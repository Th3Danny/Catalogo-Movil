package com.example.catalogoapp.src.viewCatalogo.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.catalogoapp.src.viewCatalogo.data.model.ViewCatalogoDTO
import com.example.catalogoapp.src.viewCatalogo.data.model.ViewCatalogoResponse

@Composable
fun ViewCatalogoUi(viewModel: ViewCatalogoViewModel, navController: NavController) {
    // Obtener los datos de LiveData de tipo ViewCatalogoResponse
    val catalogoResponse by viewModel.catalogo.observeAsState(ViewCatalogoResponse(emptyList()))
    val error by viewModel.error.observeAsState("")

    // Llamar a la API al cargar el Composable
    LaunchedEffect(Unit) { viewModel.fetchCatalogo() }

    val primaryRed = Color(0xFFF44336)      // Rojo brillante
    val secondaryPurple = Color(0xFF9C27B0) // Morado vibrante

    // Scaffold para incluir el botón flotante
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    // Navegar a la pantalla de creación de ropa
                    navController.navigate("create_ropa")
                },
                containerColor = primaryRed,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar prenda"
                )
            }
        }
    ) { innerPadding ->
        // Fondo degradado
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding) // Aplicar el padding del Scaffold aquí
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(primaryRed, secondaryPurple)
                    )
                )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Catálogo",
                    style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.padding(16.dp)
                )

                // Mostrar error si existe
                if (error.isNotEmpty()) {
                    Text(
                        text = "Error: $error",
                        color = Color.Yellow,
                        modifier = Modifier.padding(16.dp)
                    )
                }

                // Mostrar la lista de productos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    // Usar itemsIndexed en lugar de items
                    itemsIndexed(catalogoResponse.content) { _, item ->
                        CatalogoItem(item)
                    }
                }
            }
        }
    }
}

// Composable para cada ítem del catálogo
@Composable
fun CatalogoItem(item: ViewCatalogoDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = item.description,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Precio: $${item.price}",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(top = 8.dp)
                )

                if (item.discount_price > 0 && item.discount_price < item.price) {
                    Text(
                        text = "Oferta: $${item.discount_price}",
                        color = Color.Red,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}