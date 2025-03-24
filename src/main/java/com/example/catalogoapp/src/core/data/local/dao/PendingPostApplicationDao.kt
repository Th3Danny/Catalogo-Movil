package com.example.catalogoapp.src.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.catalogoapp.src.core.data.local.entities.PendingRopaOperationEntity


@Dao
interface PendingRopaOperationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPendingOperation(operation: PendingRopaOperationEntity): Long

    @Query("SELECT * FROM pending_ropa_operations WHERE syncStatus = 'PENDING' ORDER BY timestamp ASC")
    suspend fun getAllPendingOperations(): List<PendingRopaOperationEntity>

    @Query("UPDATE pending_ropa_operations SET syncStatus = :status, attempts = attempts + 1 WHERE id = :id")
    suspend fun updateOperationStatus(id: Long, status: String)

    @Query("DELETE FROM pending_ropa_operations WHERE id = :id")
    suspend fun deletePendingOperation(id: Long)

    @Query("SELECT * FROM pending_ropa_operations WHERE id = :id")
    suspend fun getPendingOperationById(id: Long): PendingRopaOperationEntity?

    @Query("SELECT COUNT(*) FROM pending_ropa_operations WHERE syncStatus = 'PENDING'")
    suspend fun getPendingOperationsCount(): Int
}