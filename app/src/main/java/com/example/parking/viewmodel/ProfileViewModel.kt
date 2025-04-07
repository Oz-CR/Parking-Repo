package com.example.parking.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.parking.ProfileBody
import com.example.parking.data.model.UserModel
import com.example.parking.data.respository.UsersRepository
import kotlinx.coroutines.launch

class ProfileViewModel(private val usersRepository: UsersRepository): ViewModel(){
    private val _profileData = MutableLiveData<UserModel?>()
    val profileData: LiveData<UserModel?> get() = _profileData

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun getProfile(profileBody: ProfileBody) {
        viewModelScope.launch {
            try {
                val response = usersRepository.getProfile(profileBody)
                if(response.isSuccessful) {
                    _profileData.postValue(response.body())
                } else {
                    _errorMessage.postValue("Error del servidor: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error consultando el perfil: ${e.message}")
            }
        }
    }
}