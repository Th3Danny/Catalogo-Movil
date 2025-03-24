package com.example.catalogoapp.src.register.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.firebase.messaging.FirebaseMessaging

@Composable
fun RegisterUi(
    registerViewModel: RegisterViewModel,
    navController: NavController,
    onNavigateToLogin: () -> Unit
) {
    // Observa los estados expuestos por el ViewModel
    val name by registerViewModel.email.observeAsState("")
    val password by registerViewModel.password.observeAsState("")
    val error by registerViewModel.error.observeAsState("")
    val success by registerViewModel.success.observeAsState(false)
    var isPasswordVisible by remember { mutableStateOf(false) }
    var fcmToken by remember { mutableStateOf("") }

        // Definir los colores en tonos de rojo y morado
        val primaryRed = Color(0xFFF44336)      // Rojo brillante
        val secondaryPurple = Color(0xFF9C27B0)   // Morado vibrante
    // Efecto para reaccionar ante el resultado del registro
    LaunchedEffect(Unit) {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                fcmToken = task.result ?: ""
                Log.d("FCM", "Token obtenido para registro: $fcmToken")
            } else {
                Log.e("FCM", "Error obteniendo token FCM", task.exception)
            }
        }
    }

    LaunchedEffect(success) {
        if (success) {
            onNavigateToLogin()
        }
    }

    // Fondo degradado para toda la pantalla
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
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Logo y título
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = "Logo",
                    tint = primaryRed,
                    modifier = Modifier.size(72.dp)
                )
                Text(
                    text = "Registrarse",
                    style = MaterialTheme.typography.headlineSmall,
                    color = primaryRed
                )
                // Campo de texto para el usuario
                OutlinedTextField(
                    value = name,
                    onValueChange = { registerViewModel.onChangeEmail(it) },
                    label = { Text("Usuario") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Usuario",
                            tint = primaryRed
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),

                )
                // Campo de texto para la contraseña con opción de mostrar/ocultar
                OutlinedTextField(
                    value = password,
                    onValueChange = { registerViewModel.onChangePassword(it) },
                    label = { Text("Contraseña") },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Contraseña",
                            tint = primaryRed
                        )
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = if (isPasswordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                                tint = primaryRed
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),

                )
                // Botón para registrarse
                Button(
                    onClick = { registerViewModel.registerUser(fcmToken) },
                    enabled = name.isNotEmpty() && password.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Registrarse", color = Color.White)
                }
                // Botón de cancelar
                OutlinedButton(
                    onClick = { navController.navigateUp() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = primaryRed),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(text = "Cancelar")
                }
            }
        }
    }
}
