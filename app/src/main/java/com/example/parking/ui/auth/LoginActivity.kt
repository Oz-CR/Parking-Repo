package com.example.parking.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.parking.LoginBody
import com.example.parking.data.respository.AuthRepository
import com.example.parking.ui.dashboard.DashboardActivity
import com.example.parking.databinding.ActivityLoginBinding
import com.example.parking.viewmodel.LoginViewModel
import com.example.parking.viewmodel.LoginViewModelFactory
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authRepository = AuthRepository()
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(authRepository))[LoginViewModel::class.java]

        loginViewModel.loginResult.observe(this) {result ->
            Toast.makeText(this, result, Toast.LENGTH_LONG).show()
        }

        loginViewModel.token.observe(this) {token ->
            token?.let {
                guardaTokenSP(it)
                irADashboard(it)
            }
        }

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                Toast.makeText(this, "Iniciando sesión...", Toast.LENGTH_SHORT).show()
                loginViewModel.loginUser(LoginBody(email, password))
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun guardaTokenSP(token: String?) {
        val sharedPreferences = getSharedPreferences("userToken", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("token", token)
        editor.apply()
    }

    private fun irADashboard(token: String?){
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
    }
}