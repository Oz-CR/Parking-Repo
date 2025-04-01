package com.example.parking

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface APIService {
    @POST("register")
    suspend fun registerUser(@Body registerBody: RegisterBody): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponse>

}
