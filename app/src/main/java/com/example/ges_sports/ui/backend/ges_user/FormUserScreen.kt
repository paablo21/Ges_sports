package com.example.ges_sports.ui.backend.ges_user

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ges_sports.data.DataUserRepository
import com.example.ges_sports.models.UserRoles

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormUserScreen(
    navController: NavHostController,
    userId: Int
) {
    val viewModel: GesUserViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GesUserViewModel(DataUserRepository) as T
            }
        }
    )

    val usuarioEditando = viewModel.usuarios.firstOrNull { it.id == userId }

    var nombre by remember { mutableStateOf(usuarioEditando?.nombre ?: "") }
    var email by remember { mutableStateOf(usuarioEditando?.email ?: "") }
    var password by remember { mutableStateOf(usuarioEditando?.password ?: "") }
    var rol by remember { mutableStateOf(usuarioEditando?.rol ?: "ADMIN_DEPORTIVO") }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                title = {
                    Text(
                        if (usuarioEditando == null) "Nuevo usuario" else "Editar usuario",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0288D1),
                            Color(0xFF01579B)
                        )
                    )
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF0288D1),
                            Color(0xFF000000)
                        )
                    )
                )
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // ----------- CAMPOS DE TEXTO -------------

            // NOMBRE
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x22000000),
                    unfocusedContainerColor = Color(0x22000000),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White
                )
            )

// EMAIL
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email", color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x22000000),
                    unfocusedContainerColor = Color(0x22000000),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White
                )
            )

// CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña", color = Color.White) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color(0x22000000),
                    unfocusedContainerColor = Color(0x22000000),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    cursorColor = Color.White
                )
            )


            // ----------- ROLES -------------

            Text("Rol", color = Color.White)

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                UserRoles.allRoles.forEach { (clave, etiqueta) ->
                    FilterChip(
                        selected = rol == clave,
                        onClick = { rol = clave },
                        label = { Text(etiqueta) },
                        shape = RoundedCornerShape(12.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF1A1A1A).copy(alpha = 0.5f),
                            selectedContainerColor = Color.White,
                            labelColor = Color.White,
                            selectedLabelColor = Color.Black
                        )
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // ----------- BOTÓN GUARDAR -------------

            Button(
                onClick = {
                    if (nombre.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                        if (usuarioEditando == null) {
                            viewModel.crearUsuario(nombre, email, password, rol)
                        } else {
                            viewModel.editarUsuario(
                                usuarioEditando.copy(
                                    nombre = nombre,
                                    email = email,
                                    password = password,
                                    rol = rol
                                )
                            )
                        }
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F5AA9),
                    contentColor = Color.White
                )
            ) {
                Text("Guardar")
            }
        }
    }
}
