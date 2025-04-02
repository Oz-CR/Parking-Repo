package com.example.parking.data.model

data class UserModel(
    val createdAt: String,
    val email: String,
    val id: Int,
    val password: String,
    val role: String,
    val token: String,
    val updatedAt: String,
    val username: String,
    val message: String
)