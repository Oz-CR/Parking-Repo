package com.example.parking.data.datasource

import com.example.parking.LoginBody
import com.example.parking.LoginResponse
import com.example.parking.ProfileBody
import com.example.parking.data.model.ParkingLotModel
import com.example.parking.data.model.ParkingTopicsResponse
import com.example.parking.data.model.PublishBody
import com.example.parking.data.model.RegisterBody
import com.example.parking.data.model.RegisterResponse
import com.example.parking.data.model.ResultSensors
import com.example.parking.data.model.SensorsResponse
import com.example.parking.data.model.UserModel
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIService {
    @POST("register")
    suspend fun registerUser(@Body registerBody: RegisterBody): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponse>

    @POST("get/user")
    suspend fun getProfile(@Body profileBody: ProfileBody): Response<UserModel>

    @GET("all-parking-lots")
    suspend fun getTopParkingLots(): Response<ParkingTopicsResponse>

    @POST("publish")
    suspend fun openGate(@Body publishBody: PublishBody): Response<ResponseBody>

    @GET("logs/temperatura")
    suspend fun getTemperature(): Response<ResultSensors>

    @GET("logs/humedad")
    suspend fun getHumedad(): Response<ResultSensors>

    @GET("logs/sensor_agua")
    suspend fun getAgua(): Response<ResultSensors>

    @GET("logs/sensor_luz")
    suspend fun getLuz(): Response<ResultSensors>
}