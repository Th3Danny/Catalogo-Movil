package com.example.catalogoapp.src.core.network

import com.example.catalogoapp.src.createRopa.data.datasource.CreateRopaService
import com.example.catalogoapp.src.login.data.datasource.LoginService
import com.example.catalogoapp.src.register.data.datasource.RegisterService
import com.example.catalogoapp.src.viewCatalogo.data.datasource.ViewCatalogoService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private const val BASE_URL = "http://18.205.80.182:8081/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    val loginUser: LoginService = retrofit.create(LoginService::class.java)
    val registerService: RegisterService = retrofit.create(RegisterService::class.java)
    val viewCatalogoService: ViewCatalogoService = retrofit.create(ViewCatalogoService::class.java)
    val createRopaService: CreateRopaService = retrofit.create(CreateRopaService::class.java)
}