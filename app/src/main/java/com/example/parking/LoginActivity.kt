package com.example.parking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parking.databinding.ActivityLoginBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show()
                login(LoginBody(email, password))
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://c378-2806-101e-d-a0d1-3d62-eba2-26a8-c44f.ngrok-free.app/api/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun login(loginBody: LoginBody)  {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = getRetrofit().create(APIService::class.java).login(loginBody)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loggedUser = response.body()
                        guardaTokenSP(loggedUser?.token, this@LoginActivity)
                        Toast.makeText(
                            this@LoginActivity,
                            "Iniciaste sesion correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                        irADashboard(loggedUser?.token)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@LoginActivity,
                    "Error al iniciar sesion: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun guardaTokenSP(token: String?, context: Context) {
        val sharedPreferences = context.getSharedPreferences("userToken", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun irADashboard(token: String?){
        val secretKey = "lkadjgñdsgnasjdgndngldiflkkkkkkklavgfvhjmggtñhy7u"
        try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .build()
                .parseClaimsJws(token)
                .body

            val userRole = claims["role"]?.toString() ?: "No se encontro un rol"

            if (userRole == "Admin") {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else if (userRole == "Client") {
                val intent = Intent(this, DashboardClienteActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            Toast.makeText(
                this@LoginActivity,
                "Error al iniciar sesion: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}