package com.cvelez.dittodemo.navegation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.cvelez.dittodemo.auth.AuthScreen
import com.cvelez.dittodemo.ui.TaskScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("login") { AuthScreen(navController) }
        composable("home") { TaskScreen() }
    }
}
