package com.example.parking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parking.data.respository.ParkingRepository

class TopParkingLotsViewModelFactory(private val parkingRepository: ParkingRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TopParkingLotsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TopParkingLotsViewModel(parkingRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}