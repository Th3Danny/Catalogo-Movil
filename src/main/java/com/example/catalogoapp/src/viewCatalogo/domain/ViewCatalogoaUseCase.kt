package com.example.catalogoapp.src.viewCatalogo.domain

import com.example.catalogoapp.src.viewCatalogo.data.model.ViewCatalogoDTO
import com.example.catalogoapp.src.viewCatalogo.data.repository.ViewCatalogoRepository

class ViewCatalogoUseCase(private val repository: ViewCatalogoRepository) {
    suspend operator fun invoke(): Result<List<ViewCatalogoDTO>> {
        // Obtener la respuesta del repositorio
        val response = repository.getCatalogo()

        // Verificar si la respuesta fue exitosa
        return if (response.isSuccessful) {
            // Si la respuesta es exitosa, devolvemos la lista de ViewCatalogoDTO
            val catalogoList = response.body()?.content ?: emptyList()
            Result.success(catalogoList)
        } else {
            // Si la respuesta no fue exitosa, devolvemos un error
            Result.failure(Exception("Error en la carga del catálogo"))
        }
    }
}