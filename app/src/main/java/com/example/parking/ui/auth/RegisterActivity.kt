package com.example.parking.ui.auth

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.parking.LoginBody
import com.example.parking.data.model.RegisterBody
import com.example.parking.data.respository.AuthRepository
import com.example.parking.databinding.ActivityRegisterBinding
import com.example.parking.ui.dashboard.DashboardActivity
import com.example.parking.viewmodel.LoginViewModel
import com.example.parking.viewmodel.LoginViewModelFactory
import com.example.parking.viewmodel.RegisterViewModel
import com.example.parking.viewmodel.RegisterViewModelFactory

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val authRepository = AuthRepository()
        registerViewModel = ViewModelProvider(this, RegisterViewModelFactory(authRepository))[RegisterViewModel::class.java]
        loginViewModel = ViewModelProvider(this, LoginViewModelFactory(authRepository))[LoginViewModel::class.java]

        registerViewModel.registerResult.observe(this) { result ->
            Toast.makeText(this, result, Toast.LENGTH_LONG).show()

            if (result.contains("Registro Exitoso", ignoreCase = true)) {
                val email = binding.emailcrear.text.toString()
                val password = binding.passworcuenta.text.toString()

                loginViewModel.loginUser(LoginBody(email, password))
            }
        }

        loginViewModel.loginResult.observe(this) {result ->
            Toast.makeText(this, result, Toast.LENGTH_LONG).show()
        }

        loginViewModel.token.observe(this) {token ->
            token?.let {
                guardaTokenSP(it)
                irADashboard(it)
            }
        }

        binding.registerButton.setOnClickListener {
            val username = binding.name.text.toString()
            val email = binding.emailcrear.text.toString()
            val password = binding.passworcuenta.text.toString()
            val confirmPassword = binding.confirmPasswordcuenta.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
                if (password == confirmPassword) {
                    Toast.makeText(this, "Registrando...", Toast.LENGTH_SHORT).show()
                    registerViewModel.registerUser(RegisterBody(email, password, username))
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
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