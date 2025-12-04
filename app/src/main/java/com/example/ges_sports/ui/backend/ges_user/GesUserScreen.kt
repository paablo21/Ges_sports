package com.example.ges_sports.ui.backend.ges_user

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.ges_sports.data.DataUserRepository
import com.example.ges_sports.models.User
import com.example.ges_sports.models.UserRoles

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun GesUserScreen(
    navController: NavHostController
) {

    val viewModel: GesUserViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return GesUserViewModel(DataUserRepository) as T
            }
        }
    )

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
    }

    val usuarios = viewModel.usuarios
    val filtroRol = viewModel.filtroRol

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gestión de usuarios", color = Color.White) },
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("formuser/-1")
                },
                containerColor = Color(0xFF64B5F6),
                shape = CircleShape,
                elevation = FloatingActionButtonDefaults.elevation(8.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF0288D1),
                            Color(0xFF000000)
                        )
                    )
                )
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            // CHIPS FILTRO
            // CHIPS FILTRO – 2 FILAS
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Primera fila: TODOS, Admin, Entrenador
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf(null, "ADMIN_DEPORTIVO", "ENTRENADOR").forEach { rol ->
                        FilterChip(
                            selected = filtroRol == rol,
                            onClick = { viewModel.seleccionarRol(rol) },
                            label = {
                                Text(
                                    when (rol) {
                                        null -> "TODOS"
                                        "ADMIN_DEPORTIVO" -> "Admin"
                                        "ENTRENADOR" -> "Entrenador"
                                        else -> ""
                                    }
                                )
                            },
                            border = null,
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF1A1A1A).copy(alpha = 0.6f),
                                selectedContainerColor = Color.White,
                                labelColor = Color.White,
                                selectedLabelColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Segunda fila: Árbitro, Jugador
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("ARBITRO", "JUGADOR").forEach { rol ->
                        FilterChip(
                            selected = filtroRol == rol,
                            onClick = { viewModel.seleccionarRol(rol) },
                            label = {
                                Text(
                                    when (rol) {
                                        "ARBITRO" -> "Árbitro"
                                        "JUGADOR" -> "Jugador"
                                        else -> ""
                                    }
                                )
                            },
                            border = null,
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF1A1A1A).copy(alpha = 0.6f),
                                selectedContainerColor = Color.White,
                                labelColor = Color.White,
                                selectedLabelColor = Color.Black
                            ),
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }


            Spacer(Modifier.height(4.dp))

            // LISTA
            if (usuarios.isEmpty()) {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No hay usuarios. Pulsa + para añadir.", color = Color.White)
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(usuarios) { user ->
                        TarjetaUsuario(
                            usuario = user,
                            onEditar = { u ->
                                navController.navigate("formuser/${u.id}")
                            },
                            onBorrar = { viewModel.borrarUsuario(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TarjetaUsuario(
    usuario: User,
    onEditar: (User) -> Unit,
    onBorrar: (User) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFE3F2FD)
        )
    ) {
        Row(
            Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // AVATAR
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0288D1)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    usuario.nombre.first().uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.width(14.dp))

            Column(Modifier.weight(1f)) {
                Text(
                    usuario.nombre,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF01579B)
                )
                Text(
                    usuario.rol,
                    color = Color(0xFF0288D1),
                    fontWeight = FontWeight.SemiBold
                )
            }

            IconButton(onClick = { onEditar(usuario) }) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF0288D1))
            }

            IconButton(onClick = { onBorrar(usuario) }) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}

