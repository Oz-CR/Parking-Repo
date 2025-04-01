package com.example.parking

data class ParkingLot(
    val id: Int,
    val name: String,
    val status: String,
    val distance: Double? = null,
    val lastUpdated: String? = null
)