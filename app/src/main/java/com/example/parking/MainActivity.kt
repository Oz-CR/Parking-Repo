package com.example.parking

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private val parkingLotList = mutableListOf<Item>()
    private lateinit var adapter: ItemAdapter

    // Constantes para SharedPreferences
    private val PREFS_NAME = "MyPrefs"
    private val KEY_USERNAME = "username"
    private val KEY_PASSWORD = "password"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Inicializar SharedPreferences y crear usuario por defecto
        val sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        if (!sharedPreferences.contains(KEY_USERNAME)) {
            val editor = sharedPreferences.edit()
            editor.putString(KEY_USERNAME, "admin")
            editor.putString(KEY_PASSWORD, "12345")
            editor.apply()
        }

        // Inicializar RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ItemAdapter(parkingLotList)
        recyclerView.adapter = adapter
    }
}