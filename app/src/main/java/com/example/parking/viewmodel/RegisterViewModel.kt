package com.example.parking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.parking.data.model.RegisterBody
import com.example.parking.data.respository.AuthRepository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch


class RegisterViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _registerResult = MutableLiveData<String>()
    val registerResult: LiveData<String> get() = _registerResult

    fun registerUser(registerBody: RegisterBody) {
        viewModelScope.launch {
            try {
                val response = authRepository.registerUser(registerBody)
                if (response.isSuccessful) {
                    _registerResult.postValue("Registro Exitoso")
                } else {
                    _registerResult.postValue("Error en registro: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _registerResult.postValue("Error: ${e.message}")
            }
        }
    }
}