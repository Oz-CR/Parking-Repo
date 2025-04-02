package com.example.parking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.data.model.ParkingLotModel
import com.example.parking.data.model.ParkingTopicsResponse
import com.example.parking.data.respository.ParkingRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class TopParkingLotsViewModel(private val parkingRepository: ParkingRepository): ViewModel() {
    private val _topParkingLots = MutableLiveData<Response<ParkingTopicsResponse>>()
    val topParkingLots: LiveData<Response<ParkingTopicsResponse>> get() = _topParkingLots

    fun loadTopParkingLots() {
        viewModelScope.launch {
            try {
                val result = parkingRepository.obtenerTopParkings()
                _topParkingLots.postValue(result)
            } catch(e: Exception) {
                e.printStackTrace()
            }
        }
    }
}