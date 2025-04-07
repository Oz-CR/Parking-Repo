package com.example.parking.ui.sensors

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.parking.R
import com.example.parking.databinding.ActivityInfoAguaBinding

class InfoAguaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoAguaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityInfoAguaBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}