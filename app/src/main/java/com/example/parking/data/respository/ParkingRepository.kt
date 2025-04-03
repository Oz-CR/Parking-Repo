package com.example.parking.data.respository

import com.example.parking.App
import com.example.parking.data.datasource.APIService
import com.example.parking.data.datasource.RetrofitOBJ
import com.example.parking.data.model.Datos
import com.example.parking.data.model.ParkingLotModel
import com.example.parking.data.model.ParkingTopicsResponse
import com.example.parking.data.model.PublishBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response

class ParkingRepository {
    suspend fun obtenerTopParkings(): Response<ParkingTopicsResponse> {
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitSensors(App.App.instance)
                .create(APIService::class.java)
                .getTopParkingLots()
        }
    }

    suspend fun openGate(): Response<ResponseBody> {
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitSensors(App.App.instance)
                .create(APIService::class.java)
                .openGate(PublishBody(Datos("servo", "abrir")))
        }
    }
}