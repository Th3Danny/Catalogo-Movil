package com.example.catalogoapp.src.createRopa.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CreateRopaUi(viewModel: RopaViewModel, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var talla by remember { mutableStateOf("") }

    // Definición de colores en tonos de rojo y morado
    val primaryRed = Color(0xFFF44336)      // Rojo brillante
    val secondaryPurple = Color(0xFF9C27B0)   // Morado vibrante

    // Fondo degradado para la pantalla
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(primaryRed, secondaryPurple)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Tarjeta contenedora del formulario
        Card(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Agrega una prenda",
                    style = MaterialTheme.typography.headlineSmall,
                    color = primaryRed
                )
                // Input para el nombre
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),

                )
                // Input para la descripción
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),

                )
                // Input para el precio (considerado un número entero)
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Price") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),

                )
                // Input para la talla
                OutlinedTextField(
                    value = talla,
                    onValueChange = { talla = it },
                    label = { Text("Talla") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),

                )
                // Botón para crear la prenda
                Button(
                    onClick = {
                        // Convertimos el precio a Int (si no es válido se usa -1 para no cumplir la validación)
                        val priceInt = price.toIntOrNull() ?: -1
                        if (name.isNotEmpty() && description.isNotEmpty() && priceInt >= 0 && talla.isNotEmpty()) {
                            viewModel.createRopa(name, description, priceInt, talla)
                            // Reinicia los campos (opcional)
                            name = ""
                            description = ""
                            price = ""
                            talla = ""
                            // Navega de regreso a la lista del catálogo
                            navController.navigate("view_catalogo")
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Create", color = Color.White)
                }
                // Botón para salir sin crear la prenda
                OutlinedButton(
                    onClick = { navController.navigate("view_catalogo") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Exit")
                }
            }
        }
    }
}
