package com.example.parking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.model.Log
import com.example.parking.data.respository.SensorsRepository
import kotlinx.coroutines.launch

class HumityDataViewModel(private val sensorsRepository: SensorsRepository): ViewModel() {
    private val _humidityLogs = MutableLiveData<List<Log>>()
    val humidityLogs: LiveData<List<Log>> = _humidityLogs

    fun fetchHuidityLogs() {
        viewModelScope.launch {
            try {
                val response = sensorsRepository.getHumityData()

                if(response.isSuccessful) {
                    _humidityLogs.postValue(response.body()?.logs ?: emptyList())
                } else {
                    _humidityLogs.postValue(emptyList())
                }
            } catch(e: Exception) {
                android.util.Log.e("ViewModel error", "${e.message}")
            }
        }
    }
}