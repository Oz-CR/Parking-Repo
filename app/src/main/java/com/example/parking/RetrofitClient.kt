package com.example.parking

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://f3bf-177-244-54-50.ngrok-free.app/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: APIService by lazy {
        retrofit.create(APIService::class.java)
    }

    val parkingApiService: ParkingApiService by lazy {
        retrofit.create(ParkingApiService::class.java)
    }
}