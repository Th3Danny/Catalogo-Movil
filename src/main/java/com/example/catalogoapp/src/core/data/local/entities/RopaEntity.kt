
package com.example.catalogoapp.src.core.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pending_operations")
data class RopaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val operationType: String, // "CREATE", "UPDATE", "DELETE"
    val endpoint: String,      // La URL a la que se enviaría la solicitud
    val jsonData: String,      // Datos serializados de la operación en formato JSON
    val timestamp: Long,       // Cuándo se creó la operación
    val attempts: Int = 0,     // Número de intentos de sincronización
    val syncStatus: String = "PENDING" // "PENDING", "IN_PROGRESS", "SYNCED", "ERROR"
)
