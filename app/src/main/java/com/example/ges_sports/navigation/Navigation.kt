package com.example.ges_sports.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ges_sports.ui.backend.dashboard.DashboardScreen
import com.example.ges_sports.ui.backend.ges_user.FormUserScreen
import com.example.ges_sports.ui.backend.ges_user.GesUserScreen
import com.example.ges_sports.ui.backend.ges_user.GesUserViewModel
import com.example.ges_sports.ui.backend.ges_user.GesUserViewModelFactory
import com.example.ges_sports.ui.home.HomeScreen
import com.example.ges_sports.ui.login.LoginScreen
import com.example.ges_sports.ui.screens.RegisterScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    // creas el ViewModel UNA VEZ
    val context = LocalContext.current
    val viewModel: GesUserViewModel = viewModel(
        factory = GesUserViewModelFactory(context.applicationContext)
    )

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginScreen(navController)
        }

        composable("dashboard") {
            DashboardScreen(navController)
        }

        composable("register") {
            RegisterScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("gesuser") {
            GesUserScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        // 👉 RUTA CON userId (crear / editar)
        composable(
            route = "formuser/{userId}",
            arguments = listOf(
                navArgument("userId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1

            FormUserScreen(
                navController = navController,
                viewModel = viewModel,
                userId = userId
            )
        }

        composable(
            route = "home/{nombre}",
            arguments = listOf(
                navArgument("nombre") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre")
            HomeScreen(navController, nombre)
        }
    }
}
