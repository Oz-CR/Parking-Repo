package com.example.parking.ui.sensors

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.parking.data.respository.SensorsRepository
import com.example.parking.databinding.ActivitySensoresDetailBinding
import com.example.parking.viewmodel.SensorsViewModel
import com.example.parking.viewmodel.SensoresViewModelFactory


class SensoresActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySensoresDetailBinding
    private lateinit var sensorsViewModel: SensorsViewModel

    private val sensorsRepository = SensorsRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySensoresDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sensorsViewModel = ViewModelProvider(this, SensoresViewModelFactory(sensorsRepository))
            .get(SensorsViewModel::class.java)

        sensorsViewModel.temperatureData.observe(this) { result ->
            binding.loadingTemperatura.text = "${result.text} °C"
        }

        sensorsViewModel.humedadData.observe(this) { result ->
            binding.loadingHumedad.text = "${result.text} %"
        }

        sensorsViewModel.aguaData.observe(this) { result ->
            binding.loadingAgua.text = when (result.text) {
                "0" -> "Lluvia Detectada"
                "1" -> "No se Detecta Lluvia"
                else -> "Desconocido"
            }
        }

        sensorsViewModel.luzData.observe(this) { result ->
            binding.loadingLuz.text = when (result.text) {
                "1" -> "Hay luz"
                "0" -> "Sin luz"
                else -> "Desconocido"
            }
        }

        sensorsViewModel.fetchSensorsData()

        binding.cardTemperatura.setOnClickListener() {
            val intent = Intent(this, InfoTemperaturaActivity::class.java)
            startActivity(intent)
        }

        binding.cardHumedad.setOnClickListener() {
            val intent = Intent(this, InfoHumedadActivity::class.java)
            startActivity(intent)
        }
    }
}