package com.example.catalogoapp.src.createRopa.domain

import android.util.Log
import com.example.catalogoapp.src.createRopa.data.model.RopaDTO
import com.example.catalogoapp.src.createRopa.data.repository.RopaRepository
import retrofit2.Response

/**
 * Caso de uso para crear una nueva prenda.
 */
class CreateRopaUseCase(private val repository: RopaRepository) {

    private val TAG = "CreateRopaUseCase"

    /**
     * Ejecuta el caso de uso para crear una prenda.
     * @return Un Result con la prenda creada o un error.
     */
    suspend fun execute(
        name: String,
        description: String,
        price: Int,
        talla: String
    ): Result<RopaDTO> {
        Log.d(TAG, "Ejecutando caso de uso para crear prenda: $name")

        // Validación de datos
        if (name.isBlank()) {
            return Result.failure(Exception("El nombre es obligatorio"))
        }

        if (description.isBlank()) {
            return Result.failure(Exception("La descripción es obligatoria"))
        }

        if (price <= 0) {
            return Result.failure(Exception("El precio debe ser mayor que cero"))
        }

        if (talla.isBlank()) {
            return Result.failure(Exception("La talla es obligatoria"))
        }

        // Convertir precio de Int a Double
        val priceDouble = price.toDouble()

        // Ejecutar la llamada al repositorio
        return try {
            val response = repository.createRopa(
                name = name,
                description = description,
                price = priceDouble,
                talla = talla
            )

            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear la prenda: ${response.code()} - ${response.message()}"))
            }
        } catch (e: RopaRepository.NoConnectivityException) {
            // Este es un caso especial: se guardó localmente
            Log.d(TAG, "Prenda guardada localmente para sincronización posterior")

            // Creamos un DTO ficticio para indicar que se procesó correctamente pero offline
            val offlineDTO = RopaDTO(
                id = "-1", // ID temporal negativo como string para indicar que es offline
                sku = "OFFLINE-" + System.currentTimeMillis(),
                name = name,
                description = description,
                price = priceDouble,
                discountPrice = 0.0,
                category = RopaDTO.Category(
                    id = 1,
                    name = "Pending",
                    description = null,
                    parentCategory = null,
                    subcategories = null,
                    products = null,
                    createdAt = null,
                    updatedAt = null
                ),
                brand = RopaDTO.Brand(
                    id = 1,
                    name = "Pending",
                    description = null,
                    logoUrl = null,
                    products = null,
                    createdAt = null,
                    updatedAt = null
                ),
                gender = "HOMBRE",
                material = talla,
                isActive = true,
                variants = null,
                images = null,
                tags = null,
                collections = null,
                reviews = null,
                relatedProducts = null,
                relatedToProducts = null,
                createdAt = System.currentTimeMillis().toString(),
                updatedAt = System.currentTimeMillis().toString(),
                userId = null
            )

            Result.success(offlineDTO)
        } catch (e: Exception) {
            Log.e(TAG, "Error en caso de uso: ${e.message}")
            Result.failure(e)
        }
    }
}