package com.example.ges_sports.ui.backend.ges_equipo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ges_sports.models.Equipo
import com.example.ges_sports.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GesEquipoScreen(
    navController: NavHostController,
    viewModel: GesEquipoViewModel
) {
    val equipos = viewModel.equipos
    val entrenadores = viewModel.entrenadores

    Scaffold(
        topBar = { AppTopBar("EQUIPOS") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("formequipo/-1") },
                containerColor = Color(0xFF64B5F6),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir equipo")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF0288D1), Color(0xFF000000))
                    )
                )
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (equipos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay equipos creados", color = Color.White.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(equipos) { equipo ->
                        // Buscamos el nombre del entrenador por su id
                        val nombreEntrenador = entrenadores
                            .firstOrNull { it.id == equipo.entrenadorId }
                            ?.nombre ?: "Sin entrenador"

                        EquipoCard(
                            equipo = equipo,
                            nombreEntrenador = nombreEntrenador,
                            onEdit = { navController.navigate("formequipo/${equipo.id}") },
                            onDelete = { viewModel.deleteEquipo(equipo) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EquipoCard(
    equipo: Equipo,
    nombreEntrenador: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar con inicial del nombre del equipo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0288D1)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = equipo.nombre.first().uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.width(14.dp))

            // Nombre + deporte + entrenador
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = equipo.nombre,
                    color = Color(0xFF01579B),
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = equipo.deporte,
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Entrenador: $nombreEntrenador",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onEdit) {
                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = Color(0xFF0288D1))
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
            }
        }
    }
}