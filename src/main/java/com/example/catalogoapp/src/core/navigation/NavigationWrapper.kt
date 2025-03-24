package com.example.catalogoapp.src.core.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.catalogoapp.src.core.data.local.AppDatabase
import com.example.catalogoapp.src.core.network.RetrofitHelper
import com.example.catalogoapp.src.core.network.RetrofitHelper.registerService
import com.example.catalogoapp.src.createRopa.data.repository.RopaRepository
import com.example.catalogoapp.src.createRopa.domain.CreateRopaUseCase
import com.example.catalogoapp.src.createRopa.presentation.CreateRopaUi
import com.example.catalogoapp.src.createRopa.presentation.RopaViewModel
import com.example.catalogoapp.src.createRopa.presentation.RopaViewModelFactory
import com.example.catalogoapp.src.login.data.repository.LoginRepository
import com.example.catalogoapp.src.login.domain.LoginUseCase
import com.example.catalogoapp.src.login.presentation.LoginUi
import com.example.catalogoapp.src.login.presentation.LoginViewModel
import com.example.catalogoapp.src.login.presentation.LoginViewModelFactory
import com.example.catalogoapp.src.register.data.repository.RegisterRepository
import com.example.catalogoapp.src.register.domain.CreateUserUseCase
import com.example.catalogoapp.src.register.presentation.RegisterUi
import com.example.catalogoapp.src.register.presentation.RegisterViewModel
import com.example.catalogoapp.src.register.presentation.RegisterViewModelFactory
import com.example.catalogoapp.src.viewCatalogo.presentation.ViewCatalogoFactory
import com.example.catalogoapp.src.viewCatalogo.presentation.ViewCatalogoUi

@SuppressLint("RestrictedApi")
@Composable
fun NavigationWrapper() {
    val navController = rememberNavController()
    val context = LocalContext.current // Obtener el contexto de la app

    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            val loginRepository = LoginRepository()
            val loginUseCase = LoginUseCase(loginRepository)
            val loginViewModelFactory = LoginViewModelFactory(loginUseCase, context)
            val loginViewModel: LoginViewModel = viewModel(factory = loginViewModelFactory)

            // Login UI simplificado - la navegación se maneja dentro del composable
            LoginUi(
                loginViewModel = loginViewModel,
                navController = navController
            )
        }
        composable("register") {

            val registerRepository = RegisterRepository(registerService)
            val createUserUseCase = CreateUserUseCase(registerRepository)
            val registerViewModel: RegisterViewModel = viewModel(
                factory = RegisterViewModelFactory(createUserUseCase)
            )

            RegisterUi(
                registerViewModel = registerViewModel,
                navController = navController,
                onNavigateToLogin = { navController.navigate("login") }
            )
        }

        composable("create_ropa") {
            // Obtener el contexto actual
            val context = LocalContext.current

            // Obtener la instancia de la base de datos
            val database = AppDatabase.getDatabase(context)

            // Crear las dependencias necesarias para RopaViewModel
            val ropaRepository = RopaRepository(
                api = RetrofitHelper.createRopaService,
                context = context,
                database = database
            )

            val createRopaUseCase = CreateRopaUseCase(ropaRepository)
            val ropaViewModelFactory = RopaViewModelFactory(createRopaUseCase)

            // Crear el ViewModel usando la factory
            val ropaViewModel: RopaViewModel = viewModel(factory = ropaViewModelFactory)

            CreateRopaUi(viewModel = ropaViewModel, navController = navController)
        }
        composable("view_catalogo") {
            // Usar el ViewModel desde la factory en lugar de crear uno nuevo
            val viewCatalogoViewModel = ViewCatalogoFactory.viewModel
            ViewCatalogoUi(viewModel = viewCatalogoViewModel, navController)
        }
    }
}

