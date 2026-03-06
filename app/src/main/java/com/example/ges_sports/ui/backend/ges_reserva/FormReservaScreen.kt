package com.example.ges_sports.ui.backend.ges_reserva

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ges_sports.models.Reserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormReservaScreen(
    navController: NavHostController,
    viewModel: GesReservaViewModel,
    reservaId: Int
) {
    val reservaEditando = viewModel.reservas.firstOrNull { it.id == reservaId }
    val pistas = viewModel.pistas
    val usuarios = viewModel.usuarios
    val equipos = viewModel.equipos

    var pistaSeleccionadaId by rememberSaveable { mutableStateOf(reservaEditando?.pistaId ?: -1) }
    var usuarioSeleccionadoId by rememberSaveable { mutableStateOf(reservaEditando?.usuarioId ?: -1) }
    var equipoSeleccionadoId by rememberSaveable { mutableStateOf(reservaEditando?.equipoId ?: -1) }
    var fecha by rememberSaveable { mutableStateOf(reservaEditando?.fecha ?: "") }
    var horaInicio by rememberSaveable { mutableStateOf(reservaEditando?.horaInicio ?: "") }
    var horaFin by rememberSaveable { mutableStateOf(reservaEditando?.horaFin ?: "") }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                title = {
                    Text(
                        if (reservaEditando == null) "Nueva reserva" else "Editar reserva",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                modifier = Modifier.background(
                    Brush.verticalGradient(listOf(Color(0xFF0288D1), Color(0xFF01579B)))
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .background(Brush.verticalGradient(listOf(Color(0xFF0288D1), Color(0xFF000000))))
                .padding(padding)
                .padding(20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // PISTA
            Text("Pista", color = Color.White)
            if (pistas.isEmpty()) {
                Text("No hay pistas. Crea primero una pista.", color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.bodySmall)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pistas.forEach { pista ->
                        FilterChip(
                            selected = pistaSeleccionadaId == pista.id,
                            onClick = { pistaSeleccionadaId = pista.id },
                            label = { Text("${pista.nombre} (${pista.tipo})") },
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
            }

            // USUARIO
            Text("Usuario", color = Color.White)
            if (usuarios.isEmpty()) {
                Text("No hay usuarios.", color = Color.White.copy(alpha = 0.6f), style = MaterialTheme.typography.bodySmall)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    usuarios.forEach { usuario ->
                        FilterChip(
                            selected = usuarioSeleccionadoId == usuario.id,
                            onClick = { usuarioSeleccionadoId = usuario.id },
                            label = { Text("${usuario.nombre} (${usuario.rol})") },
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
            }

            // EQUIPO (opcional)
            Text("Equipo (opcional)", color = Color.White)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Opción "Sin equipo"
                FilterChip(
                    selected = equipoSeleccionadoId == -1,
                    onClick = { equipoSeleccionadoId = -1 },
                    label = { Text("Sin equipo (reserva personal)") },
                    shape = RoundedCornerShape(12.dp),
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF1A1A1A).copy(alpha = 0.5f),
                        selectedContainerColor = Color.White,
                        labelColor = Color.White,
                        selectedLabelColor = Color.Black
                    )
                )
                equipos.forEach { equipo ->
                    FilterChip(
                        selected = equipoSeleccionadoId == equipo.id,
                        onClick = { equipoSeleccionadoId = equipo.id },
                        label = { Text(equipo.nombre) },
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

            // FECHA
            OutlinedTextField(
                value = fecha,
                onValueChange = { fecha = it },
                label = { Text("Fecha (dd/MM/yyyy)", color = Color.White) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
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

            // HORA INICIO
            OutlinedTextField(
                value = horaInicio,
                onValueChange = { horaInicio = it },
                label = { Text("Hora inicio (HH:mm)", color = Color.White) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
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

            // HORA FIN
            OutlinedTextField(
                value = horaFin,
                onValueChange = { horaFin = it },
                label = { Text("Hora fin (HH:mm)", color = Color.White) },
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
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

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color(0xFFFF6B6B))
            }

            Spacer(Modifier.height(8.dp))

            // BOTÓN GUARDAR
            Button(
                onClick = {
                    when {
                        pistaSeleccionadaId == -1 -> errorMessage = "Selecciona una pista"
                        usuarioSeleccionadoId == -1 -> errorMessage = "Selecciona un usuario"
                        fecha.isBlank() -> errorMessage = "Introduce la fecha"
                        horaInicio.isBlank() -> errorMessage = "Introduce la hora de inicio"
                        horaFin.isBlank() -> errorMessage = "Introduce la hora de fin"
                        else -> {
                            if (reservaEditando == null) {
                                viewModel.addReserva(
                                    Reserva(
                                        id = 0,
                                        pistaId = pistaSeleccionadaId,
                                        usuarioId = usuarioSeleccionadoId,
                                        equipoId = equipoSeleccionadoId,
                                        fecha = fecha,
                                        horaInicio = horaInicio,
                                        horaFin = horaFin
                                    )
                                )
                            } else {
                                viewModel.updateReserva(
                                    Reserva(
                                        id = reservaEditando.id,
                                        pistaId = pistaSeleccionadaId,
                                        usuarioId = usuarioSeleccionadoId,
                                        equipoId = equipoSeleccionadoId,
                                        fecha = fecha,
                                        horaInicio = horaInicio,
                                        horaFin = horaFin
                                    )
                                )
                            }
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F5AA9),
                    contentColor = Color.White
                )
            ) {
                Text(if (reservaEditando == null) "Crear reserva" else "Guardar cambios")
            }
        }
    }
}