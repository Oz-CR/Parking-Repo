
package com.example.parking.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.parking.ui.profile.ProfileActivity
import com.example.parking.R
import com.example.parking.data.respository.ParkingRepository
import com.example.parking.databinding.ActivityDashboardBinding
import com.example.parking.viewmodel.TopParkingLotsViewModel
import com.example.parking.viewmodel.TopParkingLotsViewModelFactory
import com.example.parking.data.model.Result
import com.example.parking.ui.sensors.SensoresActivity
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var topParkingLotsViewModel: TopParkingLotsViewModel

    private val parkingRepository = ParkingRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBtn.setOnClickListener() {
            irAPerfil()
        }

        binding.layoutParking.setOnClickListener() {
            openGate()
        }

        binding.layoutSensores.setOnClickListener() {
            val intent = Intent(this, SensoresActivity::class.java)
            startActivity(intent)
        }


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        topParkingLotsViewModel = ViewModelProvider(this, TopParkingLotsViewModelFactory(parkingRepository))
            .get(TopParkingLotsViewModel::class.java)

        topParkingLotsViewModel.topParkingLots.observe(this) {response ->
            if (response.isSuccessful) {
                response.body()?.let {parkingData ->
                    mostrarLugares(parkingData.results)
                }
            } else {
                Toast.makeText(this, "Error al obtener los datos", Toast.LENGTH_SHORT).show()
            }
        }

        topParkingLotsViewModel.loadTopParkingLots()
    }

    private fun mostrarLugares(results: List<Result>) {
        val childCount = binding.container.childCount
        for (i in 0 until minOf(childCount, results.size)) {
            val cardView = binding.container.getChildAt(i) as LinearLayout
            val cardTitle = cardView.findViewById<TextView>(R.id.card_title)
            val elementText = cardView.findViewById<TextView>(R.id.element_text)
            val card2 = cardView.findViewById<CardView>(R.id.card2)

            val parkingResult = results[i]

            cardTitle.text = parkingResult.topic

            if (parkingResult.text == "0") {
                elementText.text = "Ocupado"
                card2.setCardBackgroundColor(ContextCompat.getColor(this, android.R.color.holo_red_dark))
            } else if (parkingResult.text == "1") {
                elementText.text = "Libre"
            }
        }
    }

    private fun irAPerfil() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun openGate() {
        lifecycleScope.launch {
            try {
                val response = parkingRepository.openGate()
                if (response.isSuccessful) {
                    Toast.makeText(
                        this@DashboardActivity,
                        "Pluma abierta",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this@DashboardActivity, "Error al abrir la puerta", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@DashboardActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
        }
    }
}