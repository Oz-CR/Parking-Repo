package com.example.parking.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.R
import com.example.parking.ui.auth.LoginActivity
import com.example.parking.ui.inicio.InicioActivity

class SplashActivity : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, InicioActivity ::class.java))
            finish()
        }, 2500)
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}