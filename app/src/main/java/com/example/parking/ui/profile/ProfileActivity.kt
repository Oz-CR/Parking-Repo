package com.example.parking.ui.profile

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.parking.ProfileBody
import com.example.parking.databinding.ActivityProfileBinding
import com.example.parking.data.datasource.APIService
import com.example.parking.data.respository.UsersRepository
import com.example.parking.utils.HeaderInterceptor
import com.example.parking.utils.JWTUtils
import com.example.parking.viewmodel.ProfileViewModel
import com.example.parking.viewmodel.ProfileViewModelFactory
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val usersRepository = UsersRepository()
        profileViewModel = ViewModelProvider(this, ProfileViewModelFactory(usersRepository))[ProfileViewModel::class.java]

        val token = getTokenFromSP()
        val UserId = JWTUtils.getUserIdFromToken(token)

        profileViewModel.getProfile(ProfileBody(UserId))

        profileViewModel.profileData.observe(this) {profile ->
            binding.userName.text = "Nombre de Usuario: ${profile?.username}"
            binding.userEmail.text = "Email del Usuario: ${profile?.email}"
            binding.userRole.text = "Rol: ${profile?.role}"
        }

        profileViewModel.errorMessage.observe(this) { error ->
            binding.userName.text = error
            binding.userEmail.visibility = View.INVISIBLE
            binding.userRole.visibility = View.INVISIBLE
        }
    }

    private fun getTokenFromSP(): String{
        val sharedPreferences = getSharedPreferences("userToken", MODE_PRIVATE)
        val token: String = sharedPreferences.getString("token", "") ?: ""

        return token
    }
}