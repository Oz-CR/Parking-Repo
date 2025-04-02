package com.example.parking.data.respository

import com.example.parking.App
import com.example.parking.data.datasource.APIService
import com.example.parking.data.datasource.RetrofitOBJ
import com.example.parking.data.model.ParkingLotModel
import com.example.parking.data.model.ParkingTopicsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class ParkingRepository {
    suspend fun obtenerTopParkings(): Response<ParkingTopicsResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitSensors(App.App.instance)
                .create(APIService::class.java)
                .getTopParkingLots()
        }
    }
}