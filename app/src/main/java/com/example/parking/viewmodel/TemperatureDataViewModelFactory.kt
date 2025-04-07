package com.example.parking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.parking.data.model.Log
import com.example.parking.data.respository.SensorsRepository
import kotlinx.coroutines.launch

class TemperatureDataViewModelFactory(private val sensorsRepository: SensorsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TemperatureDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TemperatureDataViewModel(sensorsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}