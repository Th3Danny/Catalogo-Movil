package com.example.catalogoapp.src.createRopa.data.model

import com.google.gson.annotations.SerializedName

// Esta es la clase principal que enviaremos al servidor
data class CreateRopaWrapper(
    @SerializedName("id_user") val userId: Long,
    @SerializedName("product") val product: CreateRopaRequest
)

// Esta es la clase actual que ya tienes, pero ahora será parte de la estructura anidada
data class CreateRopaRequest(
    @SerializedName("sku") val sku: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("discountPrice") val discountPrice: Double,
    @SerializedName("gender") val gender: String,
    @SerializedName("material") val material: String,
    @SerializedName("isActive") val isActive: Boolean = true,
    @SerializedName("category") val category: Category,
    @SerializedName("brand") val brand: Brand
    // Quitamos el userId de aquí porque ahora está en el wrapper
) {
    data class Category(
        @SerializedName("id") val id: Int
    )

    data class Brand(
        @SerializedName("id") val id: Int
    )
}