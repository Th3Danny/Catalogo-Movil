package com.example.catalogoapp.src.login.presentation

import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.catalogoapp.src.login.domain.LoginUseCase
import kotlinx.coroutines.launch
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.catalogoapp.src.login.data.model.LoginDTO
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

open class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val context: Context
) : ViewModel() {

    private val _username = MutableLiveData<String>("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData<String>("")
    val password: LiveData<String> = _password

    private val _success = MutableLiveData(false)
    val success: LiveData<Boolean> = _success

    private val _error = MutableLiveData("")
    val error: LiveData<String> = _error

    // Flag para indicar si estamos procesando el login
    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    var loginSuccess = mutableStateOf<Boolean?>(null)

    fun loginUser(username: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.postValue(true)
                _error.postValue("")

                // Obtener el token FCM
                val fcmToken = try {
                    FirebaseMessaging.getInstance().token.await()
                } catch (e: Exception) {
                    Log.e("LoginViewModel", "Error al obtener token FCM: ${e.message}")
                    ""
                }

                val loginRequest = LoginDTO(email = username, password = password, fcm = fcmToken)

                val result = withContext(Dispatchers.IO) { loginUseCase(loginRequest) }

                result.onSuccess { loginResponse ->
                    Log.d("LoginViewModel", "Login exitoso, Token recibido")
                    Log.d("LoginViewModel", "UserId en respuesta: ${loginResponse.data.idUser}")

                    try {
                        val userIdInt = loginResponse.data.idUser.toInt()

                        // Guardar en SharedPreferences
                        saveUserData(userIdInt, loginResponse.data.token)

                        // Actualizar estado de éxito
                        _success.postValue(true)
                        _error.postValue("")
                    } catch (e: Exception) {
                        Log.e("LoginViewModel", "Error al convertir userId: ${e.message}")
                        _success.postValue(false)
                        _error.postValue("Error al procesar los datos de usuario")
                    }
                }.onFailure { exception ->
                    Log.e("LoginViewModel", "Login fallido: ${exception.message}")
                    _success.postValue(false)
                    _error.postValue("Error en login: ${exception.message}")
                }
            } catch (e: Exception) {
                Log.e("LoginViewModel", "Excepción en login: ${e.message}")
                _success.postValue(false)
                _error.postValue("Error inesperado")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }

    private fun saveUserData(userId: Int, token: String) {
        val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putInt("USER_ID", userId)
            putString("TOKEN", token)
            apply()
        }
        Log.d("LoginViewModel", "Datos guardados en SharedPreferences: USER_ID=$userId")
    }


    // Función para enviar el token FCM al backend (ahora es pública para poder llamarla después)
    fun sendFcmTokenToBackend() {
        viewModelScope.launch {
            try {
                val fcmToken = FirebaseMessaging.getInstance().token.await()
                Log.d("FCM", "Token FCM en login: $fcmToken")

                val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val authToken = sharedPreferences.getString("authToken", "")

                if (!authToken.isNullOrEmpty()) {
                    // Postpone this to avoid immediate network errors
                    com.example.catalogoapp.src.core.service.notification.FirebaseHelper.sendTokenToServer(context, fcmToken)
                } else {
                    Log.e("FCM", "No se enviará el token de FCM porque el usuario no ha iniciado sesión.")
                }
            } catch (e: Exception) {
                Log.e("FCM", "Error al obtener/enviar token de FCM: ${e.message}")
            }
        }
    }

    fun resetLoginState() {
        loginSuccess.value = null
    }

    fun onChangeUsername(username: String) {
        _username.value = username
    }

    fun onChangePassword(password: String) {
        _password.value = password
    }
}