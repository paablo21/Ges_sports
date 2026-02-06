package com.example.ges_sports.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ges_sports.R
import com.example.ges_sports.models.User
import com.example.ges_sports.ui.backend.ges_user.GesUserViewModel
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(
    navController: NavHostController,
    viewModel: GesUserViewModel
) {
    var username by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatPassword by rememberSaveable { mutableStateOf("") }
    var error by rememberSaveable { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF0288D1),
                        Color(0xFF000000)
                    )
                )
            )
            .padding(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo centro multideporte",
                modifier = Modifier.size(120.dp)
            )

            Spacer(Modifier.height(32.dp))

            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Nombre de usuario") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = repeatPassword,
                onValueChange = { repeatPassword = it },
                label = { Text("Repite tu contraseña") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {
                    error = ""

                    // Validaciones
                    if (
                        username.isBlank() ||
                        email.isBlank() ||
                        password.isBlank() ||
                        repeatPassword.isBlank()
                    ) {
                        error = "Rellena todos los campos."
                        return@Button
                    }

                    if (password != repeatPassword) {
                        error = "Las contraseñas no coinciden."
                        return@Button
                    }

                    val nuevoUsuario = User(
                        id = 0,
                        nombre = username,
                        email = email,
                        password = password,
                        rol = "ADMIN_DEPORTIVO"
                    )

                    scope.launch {
                        viewModel.addUser(nuevoUsuario)

                        navController.navigate("login") {
                            popUpTo("register") { inclusive = true }
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64B5F6),
                    contentColor = Color.Black
                )
            ) {
                Text("Registrarme")
            }

            if (error.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
