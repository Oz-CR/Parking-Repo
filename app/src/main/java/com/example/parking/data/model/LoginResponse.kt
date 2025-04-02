package com.example.parking

data class LoginResponse(
    val message: String,
    val token: String,
    val user: UserX
)