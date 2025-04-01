package com.example.parking

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        Log.d("LoginActivity", "onCreate ejecutado")

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            Log.d("LoginActivity", "Email ingresado: $email")
            Log.d("LoginActivity", "Password ingresado: $password")

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show()
                login(LoginBody(email, password))
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getRetrofit(): Retrofit {
        Log.d("LoginActivity", "Configurando Retrofit")
        return Retrofit.Builder()
            .baseUrl("https://451d-187-190-56-49.ngrok-free.app/api/auth/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun login(loginBody: LoginBody)  {
        Log.d("LoginActivity", "Iniciando login con: $loginBody")
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = getRetrofit().create(APIService::class.java).login(loginBody)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loggedUser = response.body()
                        Log.d("LoginActivity", "Login exitoso: Token recibido")
                        guardaTokenSP(loggedUser?.token, this@LoginActivity)
                        Toast.makeText(
                            this@LoginActivity,
                            "Iniciaste sesion correctamente",
                            Toast.LENGTH_LONG
                        ).show()
                        irADashboard(loggedUser?.token)
                    } else {
                        Log.e("LoginActivity", "Error en login: ${response.errorBody()?.string()}")
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginActivity", "Error al iniciar sesión: ${e.message}")
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LoginActivity,
                        "Error al iniciar sesion: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun guardaTokenSP(token: String?, context: Context) {
        Log.d("LoginActivity", "Guardando token en SharedPreferences")
        val sharedPreferences = context.getSharedPreferences("userToken", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun irADashboard(token: String?) {
        Log.d("LoginActivity", "Verificando token para Dashboard")
        val secretKey = "lkadjgñdsgnasjdgndngldiflkkkkkkklavgfvhjmggtñhy7u"
        try {
            val claims: Claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.toByteArray()))
                .build()
                .parseClaimsJws(token)
                .body

            val userRole = claims["role"]?.toString() ?: "No se encontró un rol"
            Log.d("LoginActivity", "Rol del usuario: $userRole")

            if (userRole == "Admin") {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
            } else if (userRole == "Client") {
                val intent = Intent(this, DashboardClienteActivity::class.java)
                startActivity(intent)
            }
        } catch (e: Exception) {
            Log.e("LoginActivity", "Error al verificar token: ${e.message}")
            Toast.makeText(
                this@LoginActivity,
                "Error al iniciar sesion: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
