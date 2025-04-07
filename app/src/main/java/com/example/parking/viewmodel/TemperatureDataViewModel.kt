package com.example.parking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.model.Log
import com.example.parking.data.respository.SensorsRepository
import kotlinx.coroutines.launch

class TemperatureDataViewModel(private val sensorsRepository: SensorsRepository): ViewModel() {
    private val _temperatureLogs = MutableLiveData<List<Log>>()
    val temperatureLogs: LiveData<List<Log>> = _temperatureLogs

    fun fetchTemperatureLogs() {
        viewModelScope.launch {
            try {
                val response = sensorsRepository.getTemperatureData()

                if(response.isSuccessful) {
                    _temperatureLogs.postValue(response.body()?.logs ?: emptyList())
                } else {
                    _temperatureLogs.postValue(emptyList())
                }
            } catch(e: Exception) {
                android.util.Log.e("ViewModel error", "${e.message}")
            }
        }
    }
}