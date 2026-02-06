package com.example.ges_sports.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.ges_sports.R
import com.example.ges_sports.data.RoomUserRepository
import com.example.ges_sports.database.AppDatabase
import com.example.ges_sports.domain.LogicLogin
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navController: NavHostController) {

    // Scope para lanzar corrutinas desde la UI
    val scope = rememberCoroutineScope()

    //  ROOM
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context.applicationContext) }
    val userDao = remember { db.userDao() }

    // Repo + Logic (login autenticando contra Room a través del repositorio)
    val roomRepo = remember { RoomUserRepository(userDao) }
    val logic = remember { LogicLogin(roomRepo) }

    // Estado de los campos del formulario
    var email by remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var rememberMe by rememberSaveable { mutableStateOf(false) }

    // Mensaje de error mostrado debajo de la contraseña (si falla el login)
    var errorMessage by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0288D1), // Azul arriba
                        Color(0xFF000000)  // Negro abajo
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
            // Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo centro multideporte",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Título
            Text(
                text = "CENTRO",
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 28.sp,
                    letterSpacing = 2.sp
                )
            )
            Text(
                text = "MULTIDEPORTE",
                color = Color.White,
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 32.sp,
                    letterSpacing = 2.sp
                )
            )

            Spacer(Modifier.height(32.dp))

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Usuario") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                )
            )

            Spacer(Modifier.height(8.dp))

            // Contraseña
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

            Spacer(Modifier.height(16.dp))

            // Recordar contraseña
            var recordarPassword by remember { mutableStateOf(false) }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = recordarPassword,
                    onCheckedChange = { recordarPassword = it },
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape),
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF64B5F6),
                        uncheckedColor = Color.White,
                        checkmarkColor = Color.White,
                    )
                )
                Text(
                    text = "Recordar contraseña",
                    color = Color.White,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            Spacer(Modifier.height(16.dp))

            // Login
            Button(
                onClick = {
                    scope.launch {
                        try {
                            val user = logic.comprobarLogin(email, password)

                            if (user.rol.equals("ADMIN_DEPORTIVO", ignoreCase = true)) {
                                navController.navigate("dashboard") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                navController.navigate("home/${user.nombre}") {
                                    popUpTo("login") { inclusive = true }
                                }
                            }

                            errorMessage = ""

                        } catch (e: IllegalArgumentException) {
                            errorMessage = e.message ?: "Error desconocido"
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64B5F6),
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Iniciar Sesión")
            }

            // Error
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(Modifier.height(16.dp))

            // Registro
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "¿No tienes cuenta?",
                    color = Color.White,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.width(5.dp))
                TextButton(onClick = { navController.navigate("register") }) {
                    Text(
                        text = "Regístrate",
                        color = Color(0xFF64B5F6),
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
