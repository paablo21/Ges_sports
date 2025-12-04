package com.example.ges_sports.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ges_sports.ui.login.LoginScreen
import com.example.ges_sports.ui.home.HomeScreen
import com.example.ges_sports.ui.screens.RegisterScreen
import com.example.ges_sports.ui.backend.dashboard.DashboardScreen
import com.example.ges_sports.ui.backend.ges_user.GesUserScreen
import com.example.ges_sports.ui.backend.ges_user.FormUserScreen

@Composable
fun Navigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(navController)
        }

        composable("register") {
            RegisterScreen(navController)
        }

        composable(
            route = "home/{nombre}",
            arguments = listOf(navArgument("nombre") { type = NavType.StringType })
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            HomeScreen(navController, nombre)
        }

        composable("dashboard") {
            DashboardScreen(navController)
        }

        composable("usuarios") {
            GesUserScreen(navController)
        }

        // NUEVA RUTA PARA CREAR / EDITAR USUARIO
        composable(
            route = "formuser/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            FormUserScreen(navController, id)
        }
    }
}
