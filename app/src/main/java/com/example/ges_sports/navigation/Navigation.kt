package com.example.ges_sports.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ges_sports.ui.screens.LoginScreen
import com.example.ges_sports.ui.login.home.HomeScreen
import com.example.ges_sports.ui.screens.RegisterScreen

@Composable
fun Navigation(){
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login"){
        composable("login") {LoginScreen(navController)}
        composable("register") {RegisterScreen(navController)}

        composable(
            route = "home/{nombre}",
            arguments = listOf(navArgument("nombre") {type = NavType.StringType})
        )
        {
            backStackEntry ->
            val nombre = backStackEntry.arguments?.getString("nombre")
            HomeScreen(navController, nombre)
        }
    }


}