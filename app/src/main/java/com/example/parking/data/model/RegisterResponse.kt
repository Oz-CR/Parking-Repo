package com.example.parking.data.model

import com.example.parking.User

data class RegisterResponse(
    val message: String,
    val user: User
)