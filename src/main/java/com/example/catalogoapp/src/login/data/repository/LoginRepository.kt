package com.example.catalogoapp.src.login.data.repository

import com.example.catalogoapp.src.core.network.RetrofitHelper
import com.example.catalogoapp.src.login.data.model.LoginDTO
import com.example.catalogoapp.src.login.data.model.LoginResponse


class LoginRepository {
    private val loginUser = RetrofitHelper.loginUser

    suspend fun loginUser(request: LoginDTO): Result<LoginResponse> {

        return try {
            val response = loginUser.loginUser(request)

            if (response.isSuccessful) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception(response.errorBody()?.string() ?: "Error desconocido"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}