package com.example.catalogoapp.src.createRopa.data.datasource

import com.example.catalogoapp.src.createRopa.data.model.CreateRopaWrapper
import com.example.catalogoapp.src.createRopa.data.model.RopaDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


interface CreateRopaService {
    @POST("products")
    suspend fun createRopa(@Body request: CreateRopaWrapper): Response<RopaDTO>
}