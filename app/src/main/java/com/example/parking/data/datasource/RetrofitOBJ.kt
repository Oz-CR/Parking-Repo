package com.example.parking.data.datasource

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.parking.utils.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitOBJ {
    companion object {
        private const val BASE_URL_AUTH = "https://9193-189-244-72-23.ngrok-free.app/api/auth/"
        private const val BASE_URL_USERS = "https://9193-189-244-72-23.ngrok-free.app/api/users/"
        private const val BASE_URL_PARKING = "https://9193-189-244-72-23.ngrok-free.app/api/parking/"
        private const val BASE_URL_SENSORS = "https://9193-189-244-72-23.ngrok-free.app/api/sensors/"
        private const val BASE_URL_DATA = "https://9193-189-244-72-23.ngrok-free.app/api/data/"

        fun getRetrofitAuth(context: Context): Retrofit {
            val token = context.getSharedPreferences("userToken", MODE_PRIVATE)
                .getString("token", "") ?: ""

            return createRetrofit(BASE_URL_AUTH, token)
        }

        fun getRetrofitUsers(context: Context): Retrofit {
            val token = context.getSharedPreferences("userToken", MODE_PRIVATE)
                .getString("token", "") ?: ""

            return createRetrofit(BASE_URL_USERS, token)
        }

        fun getRetrofitParking(context: Context): Retrofit {
            val token = context.getSharedPreferences("userToken", MODE_PRIVATE)
                .getString("token", "") ?: ""

            return createRetrofit(BASE_URL_PARKING, token)
        }

        fun getRetrofitSensors(context: Context): Retrofit {
            val token = context.getSharedPreferences("userToken", MODE_PRIVATE)
                .getString("token", "") ?: ""

            return createRetrofit(BASE_URL_SENSORS, token)
        }

        fun getRetrofitData(context: Context): Retrofit {
            val token = context.getSharedPreferences("userToken", MODE_PRIVATE)
                .getString("token", "") ?: ""

            return createRetrofit(BASE_URL_DATA, token)
        }

        private fun createRetrofit(baseUrl: String, token: String): Retrofit {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().addInterceptor(HeaderInterceptor(token)).build())
                .build()
        }
    }
}

