package com.example.parking.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.parking.data.respository.UsersRepository

class ProfileViewModelFactory(private val usersRepository: UsersRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(usersRepository) as T
        }

        throw IllegalArgumentException("Viewmodel desconocido")
    }
}