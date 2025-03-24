package com.example.parking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Crear credenciales por defecto si no existen
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        if (!sharedPreferences.contains("username")) {
            val editor = sharedPreferences.edit()
            editor.putString("username", "admin")
            editor.putString("password", "12345")
            editor.apply()
        }

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                if (verificarCredenciales(email, password)) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun verificarCredenciales(email: String, password: String): Boolean {
        val sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedUsername = sharedPreferences.getString("username", null)
        val savedPassword = sharedPreferences.getString("password", null)

        if (savedUsername == null || savedPassword == null) {
            Toast.makeText(this, "Error: No hay credenciales guardadas.", Toast.LENGTH_SHORT).show()
            return false
        }

        return email == savedUsername && password == savedPassword
    }
}
