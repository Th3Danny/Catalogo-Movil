package com.example.catalogoapp.src.register.data.model

data class CreateUserRequest(
    val email: String,
    val password: String,
    val fcm: String,
)