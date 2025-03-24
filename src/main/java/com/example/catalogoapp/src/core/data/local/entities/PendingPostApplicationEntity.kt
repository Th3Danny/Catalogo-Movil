package com.example.catalogoapp.src.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_ropa_operations")
data class PendingRopaOperationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val operationType: String, // "CREATE", "UPDATE", "DELETE"
    val name: String,
    val description: String,
    val price: Double,
    val talla: String,
    val sku: String,
    val discountPrice: Double = 0.0,
    val gender: String = "HOMBRE",
    val material: String,
    val categoryId: Int = 1,
    val brandId: Int = 1,
    val attempts: Int = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val syncStatus: String = "PENDING" // "PENDING", "IN_PROGRESS", "SYNCED", "ERROR"
)