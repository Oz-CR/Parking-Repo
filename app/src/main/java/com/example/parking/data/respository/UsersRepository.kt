package com.example.parking.data.respository

import com.example.parking.App
import com.example.parking.ProfileBody
import com.example.parking.data.datasource.RetrofitOBJ
import com.example.parking.data.model.UserModel
import com.example.parking.data.datasource.APIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class UsersRepository {
    suspend fun getProfile(profileBody: ProfileBody): Response<UserModel>{
        return withContext(Dispatchers.IO) {
            RetrofitOBJ.getRetrofitUsers(App.App.instance.applicationContext)
                .create(APIService::class.java)
                .getProfile(profileBody)
        }
    }
}