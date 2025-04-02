package com.example.parking.ui.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.parking.ui.profile.ProfileActivity
import com.example.parking.R
import com.example.parking.databinding.ActivityDashboardClienteBinding

class DashboardClienteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardClienteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardClienteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.profileBtn.setOnClickListener(){
            irAPerfil()
        }
    }

    private fun irAPerfil() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}