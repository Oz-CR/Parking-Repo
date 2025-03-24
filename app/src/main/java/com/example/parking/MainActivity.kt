package com.example.parking

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Configurar el RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Lista de ejemplo con datos (puedes reemplazar estas URLs con tus propias imágenes)
        val items = listOf(
            Item(
                "Parking Chido",
                "Un lugar cool para estacionarte en el centro",
                "https://images.unsplash.com/photo-1492144534655-ae79c964c9d7?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60"
            ),
            Item(
                "Estacionamiento Nice",
                "Super buen spot con seguridad 24/7",
                "https://images.unsplash.com/photo-1506521781263-d8422e7e015f?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60"
            ),
            Item(
                "Parking Pro",
                "El más rifado de la zona",
                "https://images.unsplash.com/photo-1517508736879-55003e166ee6?ixlib=rb-4.0.3&auto=format&fit=crop&w=500&q=60"
            )
        )

        // Configurar el adapter
        recyclerView.adapter = ItemAdapter(items)
    }
}