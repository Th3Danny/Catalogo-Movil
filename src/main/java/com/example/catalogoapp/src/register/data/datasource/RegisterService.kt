package com.example.catalogoapp.src.register.data.datasource

import com.example.catalogoapp.src.register.data.model.CreateUserRequest
import com.example.catalogoapp.src.register.data.model.UserDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface RegisterService {
    @POST("users")
    suspend fun registerService (@Body request: CreateUserRequest): Response<UserDTO>
}