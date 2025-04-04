package com.example.parking.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.model.Result
import com.example.parking.data.model.ResultSensors
import com.example.parking.data.respository.SensorsRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Response

class SensorsViewModel(private val sensorsRepository: SensorsRepository): ViewModel() {
    val temperatureData = MutableLiveData<ResultSensors>()
    val humedadData = MutableLiveData<ResultSensors>()
    val luzData = MutableLiveData<ResultSensors>()
    val aguaData = MutableLiveData<ResultSensors>()

    fun fetchSensorsData() {
        viewModelScope.launch {
            try {
                val tempDeferred = async { sensorsRepository.getTemperatura() }
                val humDeferred = async { sensorsRepository.getHumedad() }
                val luzDeferred = async { sensorsRepository.getLuz() }
                val aguaDeferred = async { sensorsRepository.getAgua() }

                val tempResponse = tempDeferred.await()
                val humResponse = humDeferred.await()
                val aguaResponse = aguaDeferred.await()
                val luzResponse = luzDeferred.await()

                if (tempResponse.isSuccessful) {
                    temperatureData.value = tempResponse.body()
                }

                if (humResponse.isSuccessful) {
                    humedadData.value = humResponse.body()
                }

                if (aguaResponse.isSuccessful) {
                    aguaData.value = aguaResponse.body()
                }

                if (luzResponse.isSuccessful) {
                    luzData.value = luzResponse.body()
                }

            } catch (e: Exception) {
                Log.e("ViewModel error", "${e.message}")
            }
        }
    }
}
