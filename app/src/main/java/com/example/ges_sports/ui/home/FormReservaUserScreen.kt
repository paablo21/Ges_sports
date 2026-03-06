package com.example.ges_sports.ui.home

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.ges_sports.models.Reserva
import java.util.Calendar

private val franjas = listOf(
    "09:00" to "10:30",
    "10:30" to "12:00",
    "12:00" to "13:30",
    "13:30" to "15:00",
    "15:00" to "16:30",
    "16:30" to "18:00",
    "18:00" to "19:30",
    "19:30" to "21:00",
    "21:00" to "22:30"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormReservaUserScreen(
    navController: NavHostController,
    viewModel: HomeViewModel,
    userId: Int,
    rol: String
) {
    val pistasDisponibles = viewModel.pistasDisponibles
    val equiposDelEntrenador = viewModel.equiposDeEntrenador(userId)

    var pistaSeleccionadaId by rememberSaveable { mutableStateOf(-1) }
    var equipoSeleccionadoId by rememberSaveable { mutableStateOf(-1) }
    var franjaSeleccionada by rememberSaveable { mutableStateOf(-1) }
    var errorMessage by remember { mutableStateOf("") }

    val hoy = Calendar.getInstance()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = hoy.timeInMillis,
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val inicio = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }
                return utcTimeMillis >= inicio.timeInMillis
            }
        }
    )

    val fechaFormateada = datePickerState.selectedDateMillis?.let { millis ->
        val cal = Calendar.getInstance().apply { timeInMillis = millis }
        "%02d/%02d/%04d".format(
            cal.get(Calendar.DAY_OF_MONTH),
            cal.get(Calendar.MONTH) + 1,
            cal.get(Calendar.YEAR)
        )
    } ?: ""

    val ahora = Calendar.getInstance()
    val fechaHoy = "%02d/%02d/%04d".format(
        ahora.get(Calendar.DAY_OF_MONTH),
        ahora.get(Calendar.MONTH) + 1,
        ahora.get(Calendar.YEAR)
    )

    fun franjaPasada(inicio: String): Boolean {
        if (fechaFormateada != fechaHoy) return false
        val (h, m) = inicio.split(":").map { it.toInt() }
        val horaActual = ahora.get(Calendar.HOUR_OF_DAY)
        val minActual = ahora.get(Calendar.MINUTE)
        return horaActual > h || (horaActual == h && minActual >= m)
    }

    fun franjaReservada(inicio: String): Boolean {
        if (pistaSeleccionadaId == -1 || fechaFormateada.isBlank()) return false
        return viewModel.reservas.any {
            it.pistaId == pistaSeleccionadaId &&
                    it.fecha == fechaFormateada &&
                    it.horaInicio == inicio
        }
    }

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
                        if (rol.uppercase() == "ENTRENADOR") "Reservar para el equipo"
                        else "Reservar pista",
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
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Spacer(Modifier.height(4.dp))

            // ── PISTA ──
            Text("Pista", color = Color.White, style = MaterialTheme.typography.titleMedium)
            if (pistasDisponibles.isEmpty()) {
                Text("No hay pistas disponibles.", color = Color.White.copy(alpha = 0.6f))
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    pistasDisponibles.forEach { pista ->
                        FilterChip(
                            selected = pistaSeleccionadaId == pista.id,
                            onClick = {
                                pistaSeleccionadaId = pista.id
                                franjaSeleccionada = -1  // resetear franja al cambiar pista
                            },
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

            // ── EQUIPO (solo entrenador) ──
            if (rol.uppercase() == "ENTRENADOR") {
                Text("Equipo", color = Color.White, style = MaterialTheme.typography.titleMedium)
                if (equiposDelEntrenador.isEmpty()) {
                    Text("No tienes equipos asignados.", color = Color.White.copy(alpha = 0.6f))
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        equiposDelEntrenador.forEach { equipo ->
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
                }
            }

            // ── FECHA ──
            Text("Fecha", color = Color.White, style = MaterialTheme.typography.titleMedium)
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
            ) {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        headlineContentColor = Color.White,
                        weekdayContentColor = Color.White.copy(alpha = 0.6f),
                        subheadContentColor = Color.White,
                        navigationContentColor = Color.White,
                        yearContentColor = Color.White,
                        currentYearContentColor = Color.White,
                        selectedYearContentColor = Color.Black,
                        selectedYearContainerColor = Color.White,
                        dayContentColor = Color.White,
                        selectedDayContentColor = Color.Black,
                        selectedDayContainerColor = Color.White,
                        todayContentColor = Color(0xFF64B5F6),
                        todayDateBorderColor = Color(0xFF64B5F6),
                        disabledDayContentColor = Color.White.copy(alpha = 0.2f)
                    )
                )
            }

            // ── FRANJA HORARIA ──
            Text("Franja horaria", color = Color.White, style = MaterialTheme.typography.titleMedium)

            // Leyenda
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFF1565C0)))
                    Text("Seleccionada", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(3.dp)).background(Color(0xFFE53935)))
                    Text("Ocupada", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                }
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    Box(modifier = Modifier.size(12.dp).clip(RoundedCornerShape(3.dp)).background(Color.White.copy(alpha = 0.2f)))
                    Text("Pasada", color = Color.White.copy(alpha = 0.7f), style = MaterialTheme.typography.labelSmall)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                franjas.chunked(3).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { (inicio, _) ->
                            val indexGlobal = franjas.indexOfFirst { it.first == inicio }
                            val pasada = franjaPasada(inicio)
                            val reservada = franjaReservada(inicio)
                            val bloqueada = pasada || reservada

                            FilterChip(
                                selected = franjaSeleccionada == indexGlobal,
                                onClick = { if (!bloqueada) franjaSeleccionada = indexGlobal },
                                enabled = !bloqueada,
                                label = { Text(inicio) },
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = Color(0xFF1A1A1A).copy(alpha = 0.5f),
                                    selectedContainerColor = Color(0xFF1565C0),
                                    labelColor = Color.White,
                                    selectedLabelColor = Color.White,
                                    disabledContainerColor = if (reservada)
                                        Color(0xFFE53935).copy(alpha = 0.7f)
                                    else
                                        Color.White.copy(alpha = 0.15f),
                                    disabledLabelColor = if (reservada)
                                        Color.White
                                    else
                                        Color.White.copy(alpha = 0.3f)
                                )
                            )
                        }
                    }
                }
            }

            // ── ERROR ──
            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color(0xFFFF6B6B))
            }

            Spacer(Modifier.height(8.dp))

            // ── BOTÓN CONFIRMAR ──
            Button(
                onClick = {
                    when {
                        pistaSeleccionadaId == -1 -> errorMessage = "Selecciona una pista"
                        rol.uppercase() == "ENTRENADOR" && equipoSeleccionadoId == -1 ->
                            errorMessage = "Selecciona un equipo"
                        fechaFormateada.isBlank() -> errorMessage = "Selecciona una fecha"
                        franjaSeleccionada == -1 -> errorMessage = "Selecciona una franja horaria"
                        else -> {
                            val (horaInicio, horaFin) = franjas[franjaSeleccionada]
                            viewModel.addReserva(
                                Reserva(
                                    id = 0,
                                    pistaId = pistaSeleccionadaId,
                                    usuarioId = userId,
                                    equipoId = if (rol.uppercase() == "ENTRENADOR") equipoSeleccionadoId else -1,
                                    fecha = fechaFormateada,
                                    horaInicio = horaInicio,
                                    horaFin = horaFin
                                )
                            )
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
                Text("Confirmar reserva")
            }

            Spacer(Modifier.height(16.dp))
        }
    }
}