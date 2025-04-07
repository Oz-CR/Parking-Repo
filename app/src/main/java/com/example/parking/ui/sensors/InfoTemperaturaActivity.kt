package com.example.parking.ui.sensors

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.parking.R
import com.example.parking.data.model.Log
import com.example.parking.data.respository.SensorsRepository
import com.example.parking.databinding.ActivityInfoTemperaturaBinding
import com.example.parking.viewmodel.TemperatureDataViewModel
import com.example.parking.viewmodel.TemperatureDataViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class InfoTemperaturaActivity : AppCompatActivity() {

    private lateinit var binding : ActivityInfoTemperaturaBinding
    private lateinit var temperatureDataViewModel: TemperatureDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInfoTemperaturaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sensorsRepository = SensorsRepository()
        temperatureDataViewModel = ViewModelProvider(this, TemperatureDataViewModelFactory(sensorsRepository))[TemperatureDataViewModel::class.java]

        temperatureDataViewModel.fetchTemperatureLogs()

        temperatureDataViewModel.temperatureLogs.observe(this) { logs ->
            android.util.Log.d("TEMPERATURE_DATA", "Logs recibidos: ${logs.size}")
            logs.forEach {
                android.util.Log.d("TEMPERATURE_DATA", "${it.created_at} -> ${it.log_body}")
            }
            drawTemperatureChart(logs)
        }

    }

    private fun drawTemperatureChart(logs: List<Log>) {
        val recentLogs = logs.takeLast(6)

        val entries = recentLogs.mapIndexedNotNull { index, log ->
            try {
                Entry(index.toFloat(), log.log_body.toFloat())
            } catch (e: Exception) {
                null
            }
        }

        val dataSet = LineDataSet(entries, "Temperatura (°C)").apply {
            color = Color.BLUE
            setCircleColor(Color.BLUE)
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
            valueTextColor = Color.BLACK
        }

        val lineData = LineData(dataSet)
        binding.temperatureChart.data = lineData
        binding.temperatureChart.invalidate()
    }

}