package com.example.catalogoapp.src.createRopa.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.catalogoapp.src.createRopa.domain.CreateRopaUseCase


class RopaViewModelFactory(
    private val createRopaUseCase: CreateRopaUseCase
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RopaViewModel::class.java)) {
            return RopaViewModel(createRopaUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}