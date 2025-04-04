package com.example.parking.data.respository

import com.example.parking.App
import com.example.parking.data.datasource.APIService
import com.example.parking.data.datasource.RetrofitOBJ
import com.example.parking.data.model.ParkingTopicsResponse
import com.example.parking.data.model.ResultSensors
import com.example.parking.data.model.SensorsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class SensorsRepository {
    suspend fun getTemperatura(): Response<ResultSensors> {
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitSensors(App.App.instance)
                .create(APIService::class.java)
                .getTemperature()
        }
    }

    suspend fun getAgua(): Response<ResultSensors>{
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitSensors(App.App.instance)
                .create(APIService::class.java)
                .getAgua()
        }
    }

    suspend fun getHumedad(): Response<ResultSensors> {
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitSensors(App.App.instance)
                .create(APIService::class.java)
                .getHumedad()
        }
    }

    suspend fun getLuz(): Response<ResultSensors> {
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitSensors(App.App.instance)
                .create(APIService::class.java)
                .getLuz()
        }
    }
}