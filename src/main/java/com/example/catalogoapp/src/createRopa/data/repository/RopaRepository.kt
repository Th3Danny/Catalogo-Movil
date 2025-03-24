package com.example.catalogoapp.src.createRopa.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.example.catalogoapp.src.core.data.local.AppDatabase
import com.example.catalogoapp.src.core.data.local.entities.PendingRopaOperationEntity
import com.example.catalogoapp.src.createRopa.data.datasource.CreateRopaService
import com.example.catalogoapp.src.createRopa.data.model.CreateRopaRequest
import com.example.catalogoapp.src.createRopa.data.model.CreateRopaWrapper
import com.example.catalogoapp.src.createRopa.data.model.RopaDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException

class RopaRepository(
    private val api: CreateRopaService,
    private val context: Context,
    private val database: AppDatabase
) {
    private val TAG = "RopaRepository"

    suspend fun createRopa(
        name: String,
        description: String,
        price: Double,
        talla: String,
        sku: String = "SKU-" + System.currentTimeMillis().toString().takeLast(6),
        discountPrice: Double = 0.0,
        gender: String = "HOMBRE",
        material: String = talla,
        categoryId: Int = 1,
        brandId: Int = 1
    ): Response<RopaDTO> {
        return withContext(Dispatchers.IO) {
            val isConnected = isInternetAvailable(context)

            if (!isConnected) {
                Log.d(TAG, "No hay conexión a internet. Guardando operación localmente.")

                val pendingOperation = PendingRopaOperationEntity(
                    operationType = "CREATE",
                    name = name,
                    description = description,
                    price = price,
                    talla = talla,
                    sku = sku,
                    discountPrice = discountPrice,
                    gender = gender,
                    material = material,
                    categoryId = categoryId,
                    brandId = brandId
                )

                val dao = database.pendingRopaOperationDao()
                val id = dao.insertPendingOperation(pendingOperation)

                Log.d(TAG, "Operación guardada localmente con ID: $id")
                throw NoConnectivityException("Operación guardada localmente para sincronizar más tarde.")
            }

            try {
                val sharedPreferences = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
                val userId = sharedPreferences.getInt("USER_ID", -1)

                if (userId == -1) {
                    Log.e(TAG, "Error: userId no encontrado en SharedPreferences.")
                    return@withContext Response.error(
                        500, ResponseBody.create(
                            MediaType.parse("text/plain"),
                            "Error: userId no encontrado"
                        )
                    )
                }

                val request = CreateRopaWrapper(
                    userId = userId.toLong(), // Convertimos el Int a Long para el wrapper
                    product = CreateRopaRequest(
                        name = name,
                        description = description,
                        price = price,
                        sku = sku,
                        discountPrice = discountPrice,
                        gender = gender,
                        material = material,
                        isActive = true,
                        category = CreateRopaRequest.Category(id = categoryId),
                        brand = CreateRopaRequest.Brand(id = brandId)
                    )
                )

                // Pasamos solo el objeto request, no el userId por separado
                val response = api.createRopa(request)

                if (!response.isSuccessful) {
                    val errorBody = response.errorBody()?.string() ?: "Error desconocido"
                    Log.e(TAG, "Error en API: $errorBody")
                }

                response
            } catch (e: IOException) {
                Log.e(TAG, "Error de red: ${e.message}")
                Response.error(
                    500, ResponseBody.create(
                        MediaType.parse("text/plain"),
                        "Error de red: ${e.message}"
                    )
                )
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            // Para API < 23
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManager.activeNetworkInfo
            @Suppress("DEPRECATION")
            return networkInfo != null && networkInfo.isConnected
        }
    }
    class NoConnectivityException(message: String) : IOException(message)
}

