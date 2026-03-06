package com.example.ges_sports.ui.backend.ges_equipo

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
import com.example.ges_sports.models.Equipo

private val deportes = listOf("Fútbol", "Pádel", "Tenis", "Baloncesto")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEquipoScreen(
    navController: NavHostController,
    viewModel: GesEquipoViewModel,
    equipoId: Int
) {
    val equipoEditando = viewModel.equipos.firstOrNull { it.id == equipoId }
    val entrenadores = viewModel.entrenadores
    val jugadoresDisponibles = viewModel.jugadoresDisponibles(equipoId)
    val jugadoresActuales = if (equipoId != -1) viewModel.jugadoresDeEquipo(equipoId) else emptyList()

    var nombre by rememberSaveable { mutableStateOf(equipoEditando?.nombre ?: "") }
    var deporte by rememberSaveable { mutableStateOf(equipoEditando?.deporte ?: "Fútbol") }
    var entrenadorSeleccionadoId by rememberSaveable { mutableStateOf(equipoEditando?.entrenadorId ?: -1) }
    var jugadoresSeleccionadosIds by remember {
        mutableStateOf(jugadoresActuales.map { it.id }.toSet())
    }
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
                        if (equipoEditando == null) "Nuevo equipo" else "Editar equipo",
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

            // ── NOMBRE ──
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre del equipo", color = Color.White) },
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

            // ── DEPORTE ──
            Text("Deporte", color = Color.White)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                deportes.chunked(3).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { dep ->
                            FilterChip(
                                selected = deporte == dep,
                                onClick = { deporte = dep },
                                label = { Text(dep) },
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
            }

            // ── ENTRENADOR ──
            Text("Entrenador", color = Color.White)
            if (entrenadores.isEmpty()) {
                Text(
                    "No hay entrenadores. Crea primero un usuario con rol Entrenador.",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    entrenadores.forEach { entrenador ->
                        FilterChip(
                            selected = entrenadorSeleccionadoId == entrenador.id,
                            onClick = { entrenadorSeleccionadoId = entrenador.id },
                            label = { Text(entrenador.nombre) },
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

            // ── JUGADORES ──
            Text("Jugadores", color = Color.White)
            if (jugadoresDisponibles.isEmpty()) {
                Text(
                    "No hay jugadores disponibles sin equipo.",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall
                )
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    jugadoresDisponibles.forEach { jugador ->
                        FilterChip(
                            selected = jugadoresSeleccionadosIds.contains(jugador.id),
                            onClick = {
                                jugadoresSeleccionadosIds =
                                    if (jugadoresSeleccionadosIds.contains(jugador.id))
                                        jugadoresSeleccionadosIds - jugador.id
                                    else
                                        jugadoresSeleccionadosIds + jugador.id
                            },
                            label = { Text(jugador.nombre) },
                            shape = RoundedCornerShape(12.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFF1A1A1A).copy(alpha = 0.5f),
                                selectedContainerColor = Color(0xFF43A047),
                                labelColor = Color.White,
                                selectedLabelColor = Color.White
                            )
                        )
                    }
                }
            }

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color(0xFFFF6B6B))
            }

            Spacer(Modifier.height(8.dp))

            // ── BOTÓN GUARDAR ──
            Button(
                onClick = {
                    when {
                        nombre.isBlank() -> errorMessage = "El nombre es obligatorio"
                        entrenadorSeleccionadoId == -1 -> errorMessage = "Selecciona un entrenador"
                        else -> {
                            val seleccionados = jugadoresDisponibles.filter {
                                jugadoresSeleccionadosIds.contains(it.id)
                            }
                            if (equipoEditando == null) {
                                viewModel.addEquipo(
                                    Equipo(id = 0, nombre = nombre, deporte = deporte, entrenadorId = entrenadorSeleccionadoId),
                                    seleccionados
                                )
                            } else {
                                viewModel.updateEquipo(
                                    Equipo(id = equipoEditando.id, nombre = nombre, deporte = deporte, entrenadorId = entrenadorSeleccionadoId)
                                )
                                viewModel.asignarJugadores(equipoEditando.id, seleccionados, jugadoresActuales)
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
                Text(if (equipoEditando == null) "Crear equipo" else "Guardar cambios")
            }
        }
    }
}