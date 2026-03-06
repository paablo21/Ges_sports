package com.example.ges_sports.ui.backend.ges_reserva

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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.ges_sports.models.Reserva
import com.example.ges_sports.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GesReservaScreen(
    navController: NavHostController,
    viewModel: GesReservaViewModel
) {
    val reservas = viewModel.reservas
    val pistas = viewModel.pistas
    val usuarios = viewModel.usuarios
    val equipos = viewModel.equipos

    Scaffold(
        topBar = { AppTopBar("RESERVAS") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("formreserva/-1") },
                containerColor = Color(0xFF64B5F6),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir reserva")
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Color(0xFF0288D1), Color(0xFF000000))))
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            if (reservas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay reservas", color = Color.White.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(reservas) { reserva ->
                        val nombrePista = pistas.firstOrNull { it.id == reserva.pistaId }?.nombre ?: "Pista desconocida"
                        val nombreUsuario = usuarios.firstOrNull { it.id == reserva.usuarioId }?.nombre ?: "Usuario desconocido"
                        val nombreEquipo = if (reserva.equipoId != -1)
                            equipos.firstOrNull { it.id == reserva.equipoId }?.nombre ?: "Equipo desconocido"
                        else null

                        ReservaCard(
                            reserva = reserva,
                            nombrePista = nombrePista,
                            nombreUsuario = nombreUsuario,
                            nombreEquipo = nombreEquipo,
                            onEdit = { navController.navigate("formreserva/${reserva.id}") },
                            onDelete = { viewModel.deleteReserva(reserva) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReservaCard(
    reserva: Reserva,
    nombrePista: String,
    nombreUsuario: String,
    nombreEquipo: String?,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF0288D1)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombrePista.first().uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(nombrePista, color = Color(0xFF01579B), fontWeight = FontWeight.Bold)
                Text(
                    text = "${reserva.fecha}  ·  ${reserva.horaInicio} - ${reserva.horaFin}",
                    color = Color.DarkGray,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Usuario: $nombreUsuario",
                    color = Color.Gray,
                    style = MaterialTheme.typography.bodySmall
                )
                if (nombreEquipo != null) {
                    Text(
                        text = "Equipo: $nombreEquipo",
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
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