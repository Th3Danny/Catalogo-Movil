package com.example.catalogoapp.src.register.domain


import com.example.catalogoapp.src.register.data.model.CreateUserRequest
import com.example.catalogoapp.src.register.data.repository.RegisterRepository


class CreateUserUseCase(private val repository: RegisterRepository) {
    suspend operator fun invoke(request: CreateUserRequest): Result<Unit> {
        return repository.registerUser(request)
    }
}