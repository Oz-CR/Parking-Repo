package com.example.parking.ui.dashboard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parking.R
import com.example.parking.data.model.Result
import com.example.parking.data.respository.ParkingRepository
import com.example.parking.databinding.ActivityDashboardBinding
import com.example.parking.ui.components.ParkingAdapter
import com.example.parking.ui.profile.ProfileActivity
import com.example.parking.viewmodel.TopParkingLotsViewModel
import com.example.parking.viewmodel.TopParkingLotsViewModelFactory
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import androidx.appcompat.app.AlertDialog
import com.example.parking.ui.sensors.SensoresActivity

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var topParkingLotsViewModel: TopParkingLotsViewModel
    private lateinit var parkingAdapter: ParkingAdapter
    private val parkingRepository = ParkingRepository()
    private val parkingList = mutableListOf<Result>()
    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences("ParkingPrefs", MODE_PRIVATE)

        // Cargar parkings guardados
        loadSavedParkings()

        configurarRecyclerView()
        configurarSwipeToDelete()
        configurarListeners()
        configurarSwipeRefresh()
        configurarViewModel()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun configurarRecyclerView() {
        parkingAdapter = ParkingAdapter(parkingList) { position ->
            // Callback cuando se elimina un parking
            saveParkings()
        }
        binding.recyclerViewParking.apply {
            layoutManager = LinearLayoutManager(this@DashboardActivity)
            adapter = parkingAdapter
        }
    }

    private fun configurarSwipeToDelete() {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // No permitimos mover ítems
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                parkingAdapter.removeParkingAt(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewParking)
    }

    private fun configurarListeners() {
        binding.profileBtn.setOnClickListener { irAPerfil() }

        binding.layoutParking.setOnClickListener { openGate() }
        binding.layoutSensores.setOnClickListener {
            startActivity(Intent(this, SensoresActivity::class.java))
        }

        binding.addButton.setOnClickListener {
            // Calcular el número del próximo lugar
            val nextNumber = if (parkingList.isEmpty()) {
                4 // Comenzar desde "Lugar 4" si la lista está vacía
            } else {
                // Buscar el número más alto en los nombres de los parkings
                val maxNumber = parkingList
                    .mapNotNull { parking ->
                        val numberPart = parking.topic.replace("Lugar ", "").toIntOrNull()
                        numberPart
                    }
                    .maxOrNull() ?: 3 // Si no hay números válidos, empezar desde 3
                maxNumber + 1 // Incrementar el número más alto
            }

            // Crear un nuevo parking con el nombre "Lugar X"
            val newParking = Result(
                topic = "Lugar $nextNumber",
                text = "1" // 1 = libre
            )
            parkingAdapter.addParking(newParking)
            saveParkings() // Guardar después de agregar
        }

        // Botón para eliminar: muestra un diálogo para seleccionar el parking
        binding.removeButton.setOnClickListener {
            if (parkingList.isEmpty()) {
                Toast.makeText(this, "No hay parkings para eliminar", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Crear una lista de nombres de parkings para el diálogo
            val parkingNames = parkingList.map { it.topic }.toTypedArray()

            // Mostrar un AlertDialog con la lista de parkings
            AlertDialog.Builder(this)
                .setTitle("Seleccionar Parking para Eliminar")
                .setItems(parkingNames) { _, which ->
                    // Eliminar el parking seleccionado
                    parkingAdapter.removeParkingAt(which)
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun configurarSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            // Recargar datos del ViewModel
            topParkingLotsViewModel.loadTopParkingLots()
        }
    }

    private fun configurarViewModel() {
        topParkingLotsViewModel = ViewModelProvider(
            this,
            TopParkingLotsViewModelFactory(parkingRepository)
        ).get(TopParkingLotsViewModel::class.java)

        topParkingLotsViewModel.topParkingLots.observe(this) { response ->
            if (response.isSuccessful) {
                response.body()?.let { parkingData ->
                    parkingAdapter.updateParkings(parkingData.results)
                    saveParkings() // Guardar los datos actualizados
                }
            } else {
                Toast.makeText(this, "Error al actualizar los datos", Toast.LENGTH_SHORT).show()
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

        // Cargar datos iniciales
        topParkingLotsViewModel.loadTopParkingLots()
    }

    private fun irAPerfil() {
        startActivity(Intent(this, ProfileActivity::class.java))
    }

    private fun openGate() {
        lifecycleScope.launch {
            try {
                val response = parkingRepository.openGate()
                Toast.makeText(
                    this@DashboardActivity,
                    if (response.isSuccessful) "Pluma abierta" else "Error al abrir la puerta",
                    Toast.LENGTH_LONG
                ).show()
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@DashboardActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Guardar los parkings en SharedPreferences
    private fun saveParkings() {
        val editor = sharedPreferences.edit()
        val json = gson.toJson(parkingList)
        editor.putString("parkings", json)
        editor.apply()
    }

    // Cargar los parkings desde SharedPreferences
    private fun loadSavedParkings() {
        val json = sharedPreferences.getString("parkings", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Result>>() {}.type
            val savedParkings: MutableList<Result> = gson.fromJson(json, type)
            parkingList.clear()
            parkingList.addAll(savedParkings)
        }
    }
}