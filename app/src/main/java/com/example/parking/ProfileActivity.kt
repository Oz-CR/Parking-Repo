package com.example.parking

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var userAvatar: ImageView

    // Registrar el resultado de la selección de la imagen
    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK && result.data != null) {
            val imageUri = result.data?.data
            userAvatar.setImageURI(imageUri) // Mostrar la imagen seleccionada
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Referencias a los elementos del layout
        val userNameTextView = findViewById<TextView>(R.id.userName)
        val userEmailTextView = findViewById<TextView>(R.id.userEmail)

        // Obtener datos del Intent
        val name = intent.getStringExtra("USER_NAME") ?: "Nombre no disponible"
        val email = intent.getStringExtra("USER_EMAIL") ?: "Correo no disponible"

        // Asignar datos a los TextView
        userNameTextView.text = name
        userEmailTextView.text = email


    }
}