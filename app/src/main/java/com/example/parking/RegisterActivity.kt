package com.example.parking

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.databinding.ActivityRegisterBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerButton.setOnClickListener {
            val username = binding.name.text.toString()
            val email = binding.emailcrear.text.toString()
            val password = binding.passworcuenta.text.toString()
            val confirmPassword = binding.confirmPasswordcuenta.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    Toast.makeText(this, "Registrando...", Toast.LENGTH_SHORT).show()
                    registerUser(RegisterBody(email, password, username))
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRetrofit(): Retrofit{
            return Retrofit.Builder()
                .baseUrl("https://f3bf-177-244-54-50.ngrok-free.app/api/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun registerUser(registerBody: RegisterBody) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = getRetrofit().create(APIService::class.java).registerUser(registerBody)
                withContext(Dispatchers.Main) {
                    if(response.isSuccessful) {
                        val registerResponse = response.body()
                        Toast.makeText(
                            this@RegisterActivity,
                            "${registerResponse?.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error en registro: ${response.errorBody()?.string()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@RegisterActivity, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}