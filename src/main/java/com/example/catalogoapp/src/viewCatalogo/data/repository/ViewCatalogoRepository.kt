package com.example.catalogoapp.src.viewCatalogo.data.repository


import com.example.catalogoapp.src.core.network.RetrofitHelper
import com.example.catalogoapp.src.viewCatalogo.data.model.ViewCatalogoResponse
import retrofit2.Response

class ViewCatalogoRepository {
    suspend fun getCatalogo(): Response<ViewCatalogoResponse> {
        return RetrofitHelper.viewCatalogoService.getCatalogo()
    }
}
