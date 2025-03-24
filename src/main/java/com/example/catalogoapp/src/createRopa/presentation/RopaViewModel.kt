package com.example.catalogoapp.src.createRopa.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogoapp.src.createRopa.data.model.RopaDTO
import com.example.catalogoapp.src.createRopa.domain.CreateRopaUseCase
import kotlinx.coroutines.launch

class RopaViewModel(
    private val createRopaUseCase: CreateRopaUseCase
) : ViewModel() {

    private val TAG = "RopaViewModel"

    // Estado de la operación actual
    private val _state = MutableLiveData<RopaState>()
    val state: LiveData<RopaState> = _state

    fun createRopa(name: String, description: String, price: Int, talla: String) {
        _state.value = RopaState.Loading

        viewModelScope.launch {
            try {
                Log.d(TAG, "Iniciando creación de prenda: $name")
                val result = createRopaUseCase.execute(name, description, price, talla)

                result.fold(
                    onSuccess = { ropaDTO ->
                        // Verificar si es una respuesta offline (ID negativo)
                        val idLong = ropaDTO.getIdAsLong() ?: -1
                        if (idLong < 0) {
                            Log.d(TAG, "Prenda guardada offline para sincronización posterior: $name")
                            _state.value = RopaState.OfflineSuccess(ropaDTO)
                        } else {
                            Log.d(TAG, "Prenda creada exitosamente online: $name")
                            _state.value = RopaState.Success(ropaDTO)
                        }
                    },
                    onFailure = { error ->
                        Log.e(TAG, "Error al crear prenda: ${error.message}")
                        _state.value = RopaState.Error(error.message ?: "Error desconocido")
                    }
                )
            } catch (e: Exception) {
                Log.e(TAG, "Excepción no manejada: ${e.message}")
                _state.value = RopaState.Error(e.message ?: "Error desconocido")
            }
        }
    }

    /**
     * Estados posibles para la UI.
     */
    sealed class RopaState {
        object Loading : RopaState()
        data class Success(val ropa: RopaDTO) : RopaState()
        data class OfflineSuccess(val ropa: RopaDTO) : RopaState()
        data class Error(val message: String) : RopaState()
    }
}