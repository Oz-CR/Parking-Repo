package com.example.parking.data.model

data class ParkingLotModel(
    val id: Int,
    val user_id: Int,
    val status: Boolean,
    val createdAt: String,
    val updatedAt: String
)
