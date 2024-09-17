package com.cvelez.dittodemo.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun AuthScreen(navController: NavController, viewModel: AuthViewModel = hiltViewModel()) {
    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    when (authState) {
        is AuthState.LoggedOut -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "No has iniciado sesión")
                Button(onClick = {
                    viewModel.login(context) { success ->
                        if (success) {
                            navController.navigate("home")
                        }
                    }
                }) {
                    Text(text = "Iniciar Sesión")
                }
            }
        }

        is AuthState.LoggedIn -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Sesión iniciada con éxito")
                Button(onClick = { viewModel.logout(context) }) {
                    Text(text = "Cerrar Sesión")
                }
            }
        }

        is AuthState.ProfileLoaded -> {
            val profile = (authState as AuthState.ProfileLoaded).profile
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Bienvenido, ${profile.name}")
                Button(onClick = { viewModel.logout(context) }) {
                    Text(text = "Cerrar Sesión")
                }
            }
        }

        is AuthState.Error -> {
            val errorMessage = (authState as AuthState.Error).message
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Error: $errorMessage")
                Button(onClick = {
                    viewModel.login(context) { success ->
                        if (success) {
                            navController.navigate("home")
                        }
                    }
                }) {
                    Text(text = "Reintentar")
                }
            }
        }
    }
}
