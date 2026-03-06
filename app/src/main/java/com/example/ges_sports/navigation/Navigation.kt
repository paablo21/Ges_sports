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
import com.example.ges_sports.ui.backend.ges_equipo.FormEquipoScreen
import com.example.ges_sports.ui.backend.ges_equipo.GesEquipoScreen
import com.example.ges_sports.ui.backend.ges_equipo.GesEquipoViewModel
import com.example.ges_sports.ui.backend.ges_equipo.GesEquipoViewModelFactory
import com.example.ges_sports.ui.backend.ges_pista.FormPistaScreen
import com.example.ges_sports.ui.backend.ges_pista.GesPistaScreen
import com.example.ges_sports.ui.backend.ges_pista.GesPistaViewModel
import com.example.ges_sports.ui.backend.ges_pista.GesPistaViewModelFactory
import com.example.ges_sports.ui.backend.ges_reserva.FormReservaScreen
import com.example.ges_sports.ui.backend.ges_reserva.GesReservaScreen
import com.example.ges_sports.ui.backend.ges_reserva.GesReservaViewModel
import com.example.ges_sports.ui.backend.ges_reserva.GesReservaViewModelFactory
import com.example.ges_sports.ui.backend.ges_user.FormUserScreen
import com.example.ges_sports.ui.backend.ges_user.GesUserScreen
import com.example.ges_sports.ui.backend.ges_user.GesUserViewModel
import com.example.ges_sports.ui.backend.ges_user.GesUserViewModelFactory
import com.example.ges_sports.ui.home.FormReservaUserScreen
import com.example.ges_sports.ui.home.HomeScreen
import com.example.ges_sports.ui.home.HomeViewModel
import com.example.ges_sports.ui.home.HomeViewModelFactory
import com.example.ges_sports.ui.home.LocationScreen
import com.example.ges_sports.ui.login.LoginScreen
import com.example.ges_sports.ui.screens.RegisterScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    val userViewModel: GesUserViewModel = viewModel(factory = GesUserViewModelFactory(context.applicationContext))
    val equipoViewModel: GesEquipoViewModel = viewModel(factory = GesEquipoViewModelFactory(context.applicationContext))
    val pistaViewModel: GesPistaViewModel = viewModel(factory = GesPistaViewModelFactory(context.applicationContext))
    val reservaViewModel: GesReservaViewModel = viewModel(factory = GesReservaViewModelFactory(context.applicationContext))
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(context.applicationContext))

    NavHost(navController = navController, startDestination = "login") {

        composable("login") { LoginScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("register") { RegisterScreen(navController = navController, viewModel = userViewModel) }

        // ── USUARIOS ──
        composable("gesuser") { GesUserScreen(navController = navController, viewModel = userViewModel) }
        composable(
            route = "formuser/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: -1
            FormUserScreen(navController = navController, viewModel = userViewModel, userId = userId)
        }

        // ── EQUIPOS ──
        composable("gesequipo") { GesEquipoScreen(navController = navController, viewModel = equipoViewModel) }
        composable(
            route = "formequipo/{equipoId}",
            arguments = listOf(navArgument("equipoId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val equipoId = backStackEntry.arguments?.getInt("equipoId") ?: -1
            FormEquipoScreen(navController = navController, viewModel = equipoViewModel, equipoId = equipoId)
        }

        // ── PISTAS ──
        composable("gespista") { GesPistaScreen(navController = navController, viewModel = pistaViewModel) }
        composable(
            route = "formpista/{pistaId}",
            arguments = listOf(navArgument("pistaId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val pistaId = backStackEntry.arguments?.getInt("pistaId") ?: -1
            FormPistaScreen(navController = navController, viewModel = pistaViewModel, pistaId = pistaId)
        }

        // ── RESERVAS ADMIN ──
        composable("gesreserva") { GesReservaScreen(navController = navController, viewModel = reservaViewModel) }
        composable(
            route = "formreserva/{reservaId}",
            arguments = listOf(navArgument("reservaId") { type = NavType.IntType; defaultValue = -1 })
        ) { backStackEntry ->
            val reservaId = backStackEntry.arguments?.getInt("reservaId") ?: -1
            FormReservaScreen(navController = navController, viewModel = reservaViewModel, reservaId = reservaId)
        }

        // ── RESERVAS USUARIO ──
        composable(
            route = "reservarusuario/{userId}/{rol}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("rol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val rol = backStackEntry.arguments?.getString("rol") ?: ""
            FormReservaUserScreen(navController = navController, viewModel = homeViewModel, userId = userId, rol = rol)
        }

        // ── LOCALIZACIÓN ──
        composable("location") {
            LocationScreen(navController = navController)
        }

        // ── HOME ──
        composable(
            route = "home/{userId}/{nombre}/{rol}",
            arguments = listOf(
                navArgument("userId") { type = NavType.IntType },
                navArgument("nombre") { type = NavType.StringType },
                navArgument("rol") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId") ?: 0
            val nombre = backStackEntry.arguments?.getString("nombre") ?: ""
            val rol = backStackEntry.arguments?.getString("rol") ?: ""
            HomeScreen(navController = navController, userId = userId, nombre = nombre, rol = rol)
        }
    }
}