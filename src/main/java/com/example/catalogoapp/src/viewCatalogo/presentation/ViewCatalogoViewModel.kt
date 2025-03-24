package com.example.catalogoapp.src.viewCatalogo.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogoapp.src.viewCatalogo.data.model.ViewCatalogoResponse
import com.example.catalogoapp.src.viewCatalogo.domain.ViewCatalogoUseCase
import kotlinx.coroutines.launch


class ViewCatalogoViewModel(private val getCatalogoUseCase: ViewCatalogoUseCase) : ViewModel() {

    // LiveData para exponer los datos del catálogo al UI
    private val _catalogo = MutableLiveData<ViewCatalogoResponse>()
    val catalogo: LiveData<ViewCatalogoResponse> = _catalogo

    // LiveData para manejar errores
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Función para obtener el catálogo
    fun fetchCatalogo() {
        viewModelScope.launch {
            try {
                // Llamar al caso de uso
                val result = getCatalogoUseCase()

                result.fold(
                    onSuccess = { catalogoList ->
                        // Actualizar el LiveData con la respuesta exitosa
                        _catalogo.value = ViewCatalogoResponse(catalogoList)
                        // Limpiar cualquier error previo
                        _error.value = ""
                    },
                    onFailure = { exception ->
                        // Actualizar el LiveData de error
                        _error.value = exception.message ?: "Error desconocido"
                    }
                )
            } catch (e: Exception) {
                // Capturar cualquier excepción no manejada
                _error.value = e.message ?: "Error desconocido"
            }
        }
    }
}


