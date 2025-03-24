package com.example.catalogoapp.src.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catalogoapp.src.core.data.local.entities.RopaEntity


@Dao
interface PendingOperationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRopa(ropa: RopaEntity)

    @Query("SELECT * FROM pending_operations ORDER BY timestamp DESC")
    suspend fun getAllPendingOperations(): List<RopaEntity>

    @Delete
    suspend fun deleteOperation(ropa: RopaEntity)

    @Query("SELECT COUNT(*) FROM pending_operations WHERE syncStatus = 'PENDING'")
    suspend fun getPendingOperationsCount(): Int
}