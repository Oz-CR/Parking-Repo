package com.example.parking

import retrofit2.Response
import retrofit2.http.GET

interface ParkingApiService {
        @GET("parking-status")
        suspend fun getParkingStatus(): Response<List<ParkingLot>>
    }
