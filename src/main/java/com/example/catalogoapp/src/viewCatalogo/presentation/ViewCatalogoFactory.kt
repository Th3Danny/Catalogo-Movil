package com.example.catalogoapp.src.viewCatalogo.presentation

import com.example.catalogoapp.src.viewCatalogo.data.repository.ViewCatalogoRepository
import com.example.catalogoapp.src.viewCatalogo.domain.ViewCatalogoUseCase

object ViewCatalogoFactory {
    // Crear el repositorio
    val repository: ViewCatalogoRepository by lazy {
        ViewCatalogoRepository()
    }

    // Crear el caso de uso
    val useCase: ViewCatalogoUseCase by lazy {
        ViewCatalogoUseCase(repository)
    }

    // Crear el ViewModel
    val viewModel: ViewCatalogoViewModel by lazy {
        ViewCatalogoViewModel(useCase)
    }
}