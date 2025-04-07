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
import com.example.parking.databinding.ActivityInfoHumedadBinding
import com.example.parking.viewmodel.HumityDataViewModel
import com.example.parking.viewmodel.HumityDataViewModelFactory
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class InfoHumedadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoHumedadBinding
    private lateinit var humityDataViewModel: HumityDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInfoHumedadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sensorsRepository = SensorsRepository()
        humityDataViewModel = ViewModelProvider(this, HumityDataViewModelFactory(sensorsRepository))[HumityDataViewModel::class.java]

        humityDataViewModel.fetchHuidityLogs()

        humityDataViewModel.humidityLogs.observe(this) { logs ->
            android.util.Log.d("TEMPERATURE_DATA", "Logs recibidos: ${logs.size}")
            logs.forEach {
                android.util.Log.d("TEMPERATURE_DATA", "${it.created_at} -> ${it.log_body}")
            }
            drawHumityChart(logs)
        }
    }

    private fun drawHumityChart(logs: List<Log>) {
        val recentLogs = logs.takeLast(6)

        val entries = recentLogs.mapIndexedNotNull { index, log ->
            try {
                Entry(index.toFloat(), log.log_body.toFloat())
            } catch (e: Exception) {
                null
            }
        }

        val dataSet = LineDataSet(entries, "Humedad (%)").apply {
            color = Color.BLUE
            setCircleColor(Color.BLUE)
            lineWidth = 2f
            circleRadius = 4f
            valueTextSize = 10f
            valueTextColor = Color.BLACK
        }

        val lineData = LineData(dataSet)
        binding.HumedadChart.data = lineData
        binding.HumedadChart.invalidate()
    }
}