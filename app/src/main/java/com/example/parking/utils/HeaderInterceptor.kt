package com.example.parking.utils

import okhttp3.Interceptor
import okhttp3.Response

class HeaderInterceptor(private val token: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}