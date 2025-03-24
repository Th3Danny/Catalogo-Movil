package com.example.catalogoapp.src.viewCatalogo.data.datasource


import com.example.catalogoapp.src.viewCatalogo.data.model.ViewCatalogoResponse
import retrofit2.Response
import retrofit2.http.GET

interface ViewCatalogoService {
    @GET("products")
    suspend fun getCatalogo(): Response<ViewCatalogoResponse>
}