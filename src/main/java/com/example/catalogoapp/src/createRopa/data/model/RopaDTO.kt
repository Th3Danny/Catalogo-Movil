package com.example.catalogoapp.src.createRopa.data.model

import com.google.gson.annotations.SerializedName


data class RopaDTO(
    @SerializedName("id") val id: String, // Changed to String to handle parsing
    @SerializedName("sku") val sku: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("price") val price: Double,
    @SerializedName("discountPrice") val discountPrice: Double?,
    @SerializedName("category") val category: Category,
    @SerializedName("brand") val brand: Brand,
    @SerializedName("gender") val gender: String,
    @SerializedName("material") val material: String,
    @SerializedName("isActive") val isActive: Boolean,
    @SerializedName("variants") val variants: List<Any>?, // Placeholder para futuros campos
    @SerializedName("images") val images: List<String>?,
    @SerializedName("tags") val tags: List<String>?,
    @SerializedName("collections") val collections: List<Any>?,
    @SerializedName("reviews") val reviews: List<Any>?,
    @SerializedName("related_products") val relatedProducts: List<Any>?,
    @SerializedName("related_to_products") val relatedToProducts: List<Any>?,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String,
    @SerializedName("id_user") val userId: Long? // Added userId field
) {
    data class Category(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("parent_category") val parentCategory: Any?,
        @SerializedName("subcategories") val subcategories: List<Any>?,
        @SerializedName("products") val products: List<Any>?,
        @SerializedName("created_at") val createdAt: String?,
        @SerializedName("updated_at") val updatedAt: String?
    )

    data class Brand(
        @SerializedName("id") val id: Int,
        @SerializedName("name") val name: String?,
        @SerializedName("description") val description: String?,
        @SerializedName("logo_url") val logoUrl: String?,
        @SerializedName("products") val products: List<Any>?,
        @SerializedName("created_at") val createdAt: String?,
        @SerializedName("updated_at") val updatedAt: String?
    )

    // Helper method to convert the ID string to Long
    fun getIdAsLong(): Long? {
        return id.toLongOrNull()
    }
}