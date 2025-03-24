package com.example.catalogoapp.src.login.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val data: LoginData,
    val message: String,
    val success: Boolean,
    @SerializedName("http_status")
    val httpStatus: String
) {
    data class LoginData(
        @SerializedName("access_token")
        val token: String,
        @SerializedName("refresh_token")
        val refreshToken: String,
        @SerializedName("id_user")
        val idUser: String  // Esto coincide con el campo en la respuesta JSON
    )
}