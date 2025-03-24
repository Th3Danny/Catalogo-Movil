package com.example.catalogoapp.src.register.data.repository

import android.util.Log
import com.example.catalogoapp.src.register.data.datasource.RegisterService
import com.example.catalogoapp.src.register.data.model.CreateUserRequest

class RegisterRepository(private val registerService: RegisterService) {
    suspend fun registerUser(request: CreateUserRequest) : Result<Unit>{
        return try {
            Log.d("RegisterRepository", " Enviando solicitud de registro para ${request.email}")

            val response = registerService.registerService(request)

            if (response.isSuccessful) {
                Log.d("RegisterRepository", " Registro exitoso")
                Result.success(Unit)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                Log.e("RegisterRepository", " Error en el registro: $errorMessage")
                Result.failure(Exception(errorMessage))
            }
        } catch (e: Exception) {
            Log.e("RegisterRepository", " Excepción en el registro: ${e.message}")
            Result.failure(e)
        }
    }

}