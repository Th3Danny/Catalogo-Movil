package com.example.catalogoapp.src.login.domain

import com.example.catalogoapp.src.login.data.model.LoginDTO
import com.example.catalogoapp.src.login.data.model.LoginResponse
import com.example.catalogoapp.src.login.data.repository.LoginRepository


class LoginUseCase(private val repository: LoginRepository) {


    suspend operator fun invoke(request: LoginDTO): Result<LoginResponse> {
        return try {
            repository.loginUser(request)
        } catch (e: Exception) {
            // Manejo de excepciones
            Result.failure(e)
        }
    }

}