package com.example.catalogoapp.src.register.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.catalogoapp.src.register.domain.CreateUserUseCase
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catalogoapp.src.register.data.model.CreateUserRequest

class RegisterViewModel(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _email = MutableLiveData("")
    val email: LiveData<String> = _email

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password

    private val _success = MutableLiveData(false)
    val success: LiveData<Boolean> = _success

    private val _error = MutableLiveData("")
    val error: LiveData<String> = _error

    fun onChangeEmail(email: String) {
        _email.value = email
    }

    fun onChangePassword(password: String) {
        _password.value = password
    }

    fun registerUser(fcmToken: String) {
        viewModelScope.launch {
            Log.d("RegisterViewModel", " Intentando registrar usuario: ${_email.value}")

            val request = CreateUserRequest(
                email = _email.value ?: "",
                password = _password.value ?: "",
                fcm = fcmToken,
            )
            Log.d("RegisterViewModel", " Payload de Registro: $request")

            val result = createUserUseCase(request)
            result.onSuccess {
                Log.d("RegisterViewModel", " Registro exitoso para ${request.email}")
                _success.value = true
                _error.value = ""
            }.onFailure { exception ->
                Log.e("RegisterViewModel", " Error en el registro: ${exception.message}")
                _success.value = false
                _error.value = exception.message ?: "Error desconocido"
            }
        }
    }
}

