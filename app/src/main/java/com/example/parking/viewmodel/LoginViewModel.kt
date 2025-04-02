package com.example.parking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.LoginBody
import com.example.parking.data.respository.AuthRepository
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository): ViewModel() {
    private val _loginResult = MutableLiveData<String>()
    val loginResult: LiveData<String> get() = _loginResult

    private val _token = MutableLiveData<String?>()
    val token: LiveData<String?> get() = _token

    fun loginUser(loginBody: LoginBody) {
        viewModelScope.launch {
            try {
                val response = authRepository.login(loginBody)
                if (response.isSuccessful) {
                    val loggedUser = response.body()
                    _token.postValue(loggedUser?.token)
                    _loginResult.postValue("Iniciaste sesion correctamente")
                }
            } catch (e: Exception) {
                _loginResult.postValue("Error iniciando sesion, ${e.message}")
            }
        }
    }
}