package com.example.parking.data.model

data class Log(
    val created_at: String,
    val id: Int,
    val log_body: String,
    val sensor_id: Int,
    val updated_at: String
)