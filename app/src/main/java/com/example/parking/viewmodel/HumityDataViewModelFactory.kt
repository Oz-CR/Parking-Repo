package com.example.parking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parking.data.respository.SensorsRepository

class HumityDataViewModelFactory(private val sensorsRepository: SensorsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HumityDataViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HumityDataViewModel(sensorsRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}