package com.example.parking.data.datasource

import android.content.Context
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.example.parking.utils.HeaderInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitOBJ {
    companion object {
        private const val BASE_URL_AUTH = "http://3.147.69.4:3000/api/auth/"
        private const val BASE_URL_USERS = "http://3.147.69.4:3000/api/users/"
        private const val BASE_URL_PARKING = "http://3.147.69.4:3000/api/parking/"
        private const val BASE_URL_SENSORS = "http://3.147.69.4:3000/api/sensors/"

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
            val token = context.getSharedPreferences("usertToken", MODE_PRIVATE)
                .getString("token", "") ?: ""

            return createRetrofit(BASE_URL_SENSORS, token)
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

