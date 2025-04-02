package com.example.parking.data.respository

import com.example.parking.App
import com.example.parking.LoginBody
import com.example.parking.LoginResponse
import com.example.parking.data.datasource.RetrofitOBJ
import com.example.parking.data.model.RegisterBody
import com.example.parking.data.model.RegisterResponse
import com.example.parking.data.datasource.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class AuthRepository {
    suspend fun registerUser(registerBody: RegisterBody): Response<RegisterResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitAuth(App.App.instance.applicationContext)
                .create(APIService::class.java)
                .registerUser(registerBody)
        }
    }

    suspend fun login(loginBody: LoginBody): Response<LoginResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitAuth(App.App.instance.applicationContext)
                .create(APIService::class.java)
                .login(loginBody)
        }
    }
}