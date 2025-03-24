package com.example.catalogoapp.src.register.presentation


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.catalogoapp.src.register.domain.CreateUserUseCase

class RegisterViewModelFactory(private val createUserUseCase: CreateUserUseCase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RegisterViewModel(createUserUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
