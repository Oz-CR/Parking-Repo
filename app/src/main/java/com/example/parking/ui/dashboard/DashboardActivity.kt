package com.example.parking.ui.dashboard

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
    private val parkingList = mutableListOf<Result>() // Lista completa de parkings (servidor + locales)
    private val localParkings = mutableListOf<Result>() // Lista de parkings creados localmente
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
            try {
                // No necesitamos acceder a parkingList[position] aquí, ya que la eliminación ya ocurrió
                // Solo actualizamos localParkings y guardamos los cambios
                // Sincronizamos localParkings con parkingList
                localParkings.removeAll { localParking ->
                    !parkingList.contains(localParking)
                }
                saveParkings()
            } catch (e: Exception) {
                Log.e("DashboardActivity", "Error al actualizar localParkings: ${e.message}", e)
                Toast.makeText(this, "Error al eliminar el parking", Toast.LENGTH_SHORT).show()
            }
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
                // Validar que la posición sea válida antes de eliminar
                if (position in 0 until parkingList.size) {
                    parkingAdapter.removeParkingAt(position)
                } else {
                    Log.e("DashboardActivity", "Posición inválida al deslizar: $position")
                    Toast.makeText(this@DashboardActivity, "Error al eliminar el parking", Toast.LENGTH_SHORT).show()
                    parkingAdapter.notifyDataSetChanged() // Refrescar el adaptador para evitar inconsistencias
                }
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
            // Calcular el número del próximo lugar, comenzando desde "Lugar 4"
            val nextNumber = if (parkingList.isEmpty()) {
                4 // Comenzar desde "Lugar 4"
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
            localParkings.add(newParking) // Agregar a la lista de parkings locales
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
                    try {
                        // Validar que el índice sea válido
                        if (which in 0 until parkingList.size) {
                            parkingAdapter.removeParkingAt(which)
                        } else {
                            Log.e("DashboardActivity", "Índice inválido al eliminar desde diálogo: $which")
                            Toast.makeText(this, "Error al eliminar el parking", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Log.e("DashboardActivity", "Error al eliminar desde diálogo: ${e.message}", e)
                        Toast.makeText(this, "Error al eliminar el parking", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun configurarSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
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
                    // Combinar los parkings del servidor con los parkings locales
                    val serverParkings = parkingData.results.toMutableList()
                    val combinedParkings = mutableListOf<Result>()

                    // Agregar los parkings del servidor
                    combinedParkings.addAll(serverParkings)

                    // Agregar los parkings locales que no están en el servidor
                    localParkings.forEach { localParking ->
                        if (combinedParkings.none { it.topic == localParking.topic }) {
                            combinedParkings.add(localParking)
                        }
                    }

                    // Actualizar la lista del adaptador
                    parkingAdapter.updateParkings(combinedParkings)
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
        try {
            val editor = sharedPreferences.edit()
            val json = gson.toJson(parkingList)
            val localJson = gson.toJson(localParkings)
            editor.putString("parkings", json)
            editor.putString("local_parkings", localJson) // Guardar también los parkings locales
            editor.apply()
        } catch (e: Exception) {
            Log.e("DashboardActivity", "Error al guardar parkings: ${e.message}", e)
            Toast.makeText(this, "Error al guardar los datos", Toast.LENGTH_SHORT).show()
        }
    }

    // Cargar los parkings desde SharedPreferences
    private fun loadSavedParkings() {
        try {
            val json = sharedPreferences.getString("parkings", null)
            val localJson = sharedPreferences.getString("local_parkings", null)
            if (json != null) {
                val type = object : TypeToken<MutableList<Result>>() {}.type
                val savedParkings: MutableList<Result> = gson.fromJson(json, type)
                parkingList.clear()
                parkingList.addAll(savedParkings)
            }
            if (localJson != null) {
                val type = object : TypeToken<MutableList<Result>>() {}.type
                val savedLocalParkings: MutableList<Result> = gson.fromJson(localJson, type)
                localParkings.clear()
                localParkings.addAll(savedLocalParkings)
            }
        } catch (e: Exception) {
            Log.e("DashboardActivity", "Error al cargar parkings: ${e.message}", e)
            Toast.makeText(this, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
        }
    }
}