package com.example.catalogoapp.src.core.workers

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.catalogoapp.src.core.data.local.AppDatabase
import com.example.catalogoapp.src.createRopa.data.datasource.CreateRopaService
import com.example.catalogoapp.src.createRopa.data.repository.RopaRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SyncRopaOperationsWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    private val TAG = "SyncRopaWorker"
    private val database = AppDatabase.getDatabase(context)
    private val pendingRopaOperationDao = database.pendingRopaOperationDao()

    // Idealmente este servicio se inyectaría, pero para el ejemplo lo creamos aquí
    private val api: CreateRopaService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://18.205.80.182:8081/api/") // Reemplazar con tu URL base real
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(CreateRopaService::class.java)
    }

    private val repository = RopaRepository(
        api = api,
        context = applicationContext,
        database = database
    )

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Iniciando sincronización de operaciones pendientes...")

            val pendingOperations = pendingRopaOperationDao.getAllPendingOperations()

            if (pendingOperations.isEmpty()) {
                Log.d(TAG, "No hay operaciones pendientes para sincronizar")
                return@withContext Result.success()
            }

            Log.d(TAG, "Encontradas ${pendingOperations.size} operaciones pendientes")

            var successCount = 0
            var failureCount = 0

            for (operation in pendingOperations) {
                try {
                    // Marcar la operación como en progreso
                    pendingRopaOperationDao.updateOperationStatus(operation.id, "IN_PROGRESS")

                    if (operation.operationType == "CREATE") {
                        Log.d(TAG, "Sincronizando creación de prenda: ${operation.name}")

                        val response = repository.createRopa(
                            name = operation.name,
                            description = operation.description,
                            price = operation.price,
                            talla = operation.talla,
                            sku = operation.sku,
                            discountPrice = operation.discountPrice,
                            gender = operation.gender,
                            material = operation.material,
                            categoryId = operation.categoryId,
                            brandId = operation.brandId
                        )

                        if (response.isSuccessful) {
                            Log.d(TAG, "Sincronización exitosa para: ${operation.id}")
                            pendingRopaOperationDao.deletePendingOperation(operation.id)
                            successCount++
                        } else {
                            // Si la respuesta no es exitosa, marcar como error
                            val errorMessage = response.errorBody()?.string() ?: "Error desconocido"
                            Log.e(TAG, "Error al sincronizar: ${response.code()} - $errorMessage")
                            pendingRopaOperationDao.updateOperationStatus(operation.id, "ERROR")
                            failureCount++
                        }
                    }
                    // Aquí se pueden agregar más tipos de operaciones como UPDATE, DELETE, etc.

                } catch (e: Exception) {
                    Log.e(TAG, "Excepción al sincronizar operación ${operation.id}: ${e.message}")
                    pendingRopaOperationDao.updateOperationStatus(operation.id, "ERROR")
                    failureCount++
                }
            }

            val resultData = workDataOf(
                "success_count" to successCount,
                "failure_count" to failureCount
            )

            if (failureCount > 0) {
                Log.d(TAG, "Sincronización completada con errores: $successCount éxitos, $failureCount fallos")
                Result.failure(resultData)
            } else {
                Log.d(TAG, "Sincronización completada con éxito: $successCount operaciones")
                Result.success(resultData)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error general en el worker: ${e.message}")
            Result.retry()
        }
    }
}