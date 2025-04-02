package com.example.parking.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.parking.ui.profile.ProfileActivity
import com.example.parking.R
import com.example.parking.data.model.ParkingLotModel
import com.example.parking.data.respository.ParkingRepository
import com.example.parking.databinding.ActivityDashboardBinding
import com.example.parking.viewmodel.TopParkingLotsViewModel
import com.example.parking.viewmodel.TopParkingLotsViewModelFactory

class DashboardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding
    private lateinit var topParkingLotsViewModel: TopParkingLotsViewModel
    private lateinit var linearLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.profileBtn.setOnClickListener() {
            irAPerfil()
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        linearLayout = binding.container
    }

    private fun mostrarLugares() {

    }

    private fun irAPerfil() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}