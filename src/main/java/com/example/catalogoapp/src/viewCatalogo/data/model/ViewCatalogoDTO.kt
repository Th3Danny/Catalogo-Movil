package com.example.catalogoapp.src.viewCatalogo.data.model

data class ViewCatalogoDTO(
    val id: Int,
    val sku: String,
    val name: String,
    val description: String,
    val price: Double,
    val discount_price: Double,
    val category_id: Int,
    val category_name: String,
    val brand_id: Int,
    val brand_name: String,
    val gender: String,
    val material: String,
    val is_active: Boolean,
    val images: List<String>,
    val created_at: String,
    val updated_at: String
)
