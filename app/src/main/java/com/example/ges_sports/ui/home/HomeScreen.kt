package com.example.ges_sports.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ges_sports.models.Pista
import com.example.ges_sports.models.Reserva

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    userId: Int,
    nombre: String,
    rol: String
) {
    val context = LocalContext.current
    val homeViewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory(context.applicationContext)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF0288D1), Color(0xFF000000))))
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Centro Multideporte", color = Color.White, fontSize = 20.sp) },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate("login") { popUpTo(0) { inclusive = true } }
                        }) {
                            Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Spacer(Modifier.height(4.dp))
                TarjetaBienvenida(nombre = nombre, rol = rol)
                when (rol.uppercase()) {
                    "JUGADOR"    -> ContenidoJugador(navController, userId, homeViewModel)
                    "ENTRENADOR" -> ContenidoEntrenador(navController, userId, homeViewModel)
                    else         -> ContenidoGenerico()
                }
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

// ─────────────────────────────────────────
// BIENVENIDA
// ─────────────────────────────────────────
@Composable
fun TarjetaBienvenida(nombre: String, rol: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f))
    ) {
        Row(modifier = Modifier.padding(20.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(56.dp).clip(CircleShape).background(Color(0xFF64B5F6)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = nombre.firstOrNull()?.uppercase() ?: "?",
                    color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text("¡Hola, $nombre!", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(4.dp))
                val (etiqueta, colorRol) = when (rol.uppercase()) {
                    "JUGADOR"    -> "Jugador"    to Color(0xFF43A047)
                    "ENTRENADOR" -> "Entrenador" to Color(0xFF1565C0)
                    "ARBITRO"    -> "Árbitro"    to Color(0xFF6A1B9A)
                    else         -> rol           to Color.Gray
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(colorRol)
                        .padding(horizontal = 10.dp, vertical = 3.dp)
                ) {
                    Text(etiqueta, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

// ─────────────────────────────────────────
// CONTENIDO JUGADOR
// ─────────────────────────────────────────
@Composable
fun ContenidoJugador(navController: NavController, userId: Int, viewModel: HomeViewModel) {
    val reservasPropias = viewModel.reservasDeUsuario(userId)
    val equipoDelJugador = viewModel.equipoDeJugador(userId)
    val reservasEquipo = if (equipoDelJugador != null)
        viewModel.reservasDeEquipo(equipoDelJugador.id) else emptyList()
    val asistencia = viewModel.asistenciaDeJugador(userId)

    // Botón reservar
    SeccionTitulo(icono = Icons.Default.Star, titulo = "Mis acciones")
    TarjetaAccion(
        modifier = Modifier.fillMaxWidth(),
        icono = Icons.Default.DateRange,
        titulo = "Reservar pista",
        subtitulo = "Nueva reserva personal",
        color = Color(0xFF1565C0),
        onClick = { navController.navigate("reservarusuario/$userId/JUGADOR") }
    )

    // Reservas personales
    SeccionTitulo(icono = Icons.Default.DateRange, titulo = "Mis próximas reservas")
    TarjetaListaReservas(
        reservas = reservasPropias,
        todasLasPistas = viewModel.todasLasPistas,
        mensajeVacio = "No tienes reservas próximas",
        onCancelar = { viewModel.deleteReserva(it) }
    )

    // Reservas de equipo
    if (equipoDelJugador != null) {
        SeccionTitulo(icono = Icons.Default.Person, titulo = "Reservas de ${equipoDelJugador.nombre}")
        TarjetaReservasEquipoJugador(
            reservas = reservasEquipo,
            todasLasPistas = viewModel.todasLasPistas,
            asistencia = asistencia,
            onAsistir = { viewModel.actualizarAsistencia(userId, "ASISTE") },
            onNoAsistir = { viewModel.actualizarAsistencia(userId, "NO_ASISTE") }
        )
    } else {
        SeccionTitulo(icono = Icons.Default.Person, titulo = "Mi equipo")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
        ) {
            Text(
                "No perteneces a ningún equipo todavía.",
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    // Localización
    SeccionTitulo(icono = Icons.Default.LocationOn, titulo = "Localización del centro")
    TarjetaLocalizacion(onClick = { navController.navigate("location") })
}

// ─────────────────────────────────────────
// CONTENIDO ENTRENADOR
// ─────────────────────────────────────────
@Composable
fun ContenidoEntrenador(navController: NavController, userId: Int, viewModel: HomeViewModel) {
    val equiposDelEntrenador = viewModel.equiposDeEntrenador(userId)

    SeccionTitulo(icono = Icons.Default.Star, titulo = "Mis acciones")
    TarjetaAccion(
        modifier = Modifier.fillMaxWidth(),
        icono = Icons.Default.DateRange,
        titulo = "Reservar para el equipo",
        subtitulo = "Nueva reserva de equipo",
        color = Color(0xFF1565C0),
        onClick = { navController.navigate("reservarusuario/$userId/ENTRENADOR") }
    )

    equiposDelEntrenador.forEach { equipo ->
        val reservasEquipo = viewModel.reservasDeEquipo(equipo.id)
        SeccionTitulo(icono = Icons.Default.Person, titulo = "Reservas: ${equipo.nombre}")
        TarjetaListaReservas(
            reservas = reservasEquipo,
            todasLasPistas = viewModel.todasLasPistas,
            mensajeVacio = "No hay reservas para este equipo",
            onCancelar = { viewModel.deleteReserva(it) }
        )
    }

    if (equiposDelEntrenador.isEmpty()) {
        SeccionTitulo(icono = Icons.Default.Person, titulo = "Mis equipos")
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
        ) {
            Text(
                "No tienes equipos asignados.",
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(16.dp)
            )
        }
    }

    SeccionTitulo(icono = Icons.Default.Warning, titulo = "Incidencias")
    TarjetaIncidencias(onClick = { })

    // Localización
    SeccionTitulo(icono = Icons.Default.LocationOn, titulo = "Localización del centro")
    TarjetaLocalizacion(onClick = { navController.navigate("location") })
}

// ─────────────────────────────────────────
// CONTENIDO GENÉRICO
// ─────────────────────────────────────────
@Composable
fun ContenidoGenerico() {
    SeccionTitulo(icono = Icons.Default.Info, titulo = "Bienvenido al Centro Multideporte")
}

// ─────────────────────────────────────────
// COMPONENTES REUTILIZABLES
// ─────────────────────────────────────────

@Composable
fun SeccionTitulo(icono: ImageVector, titulo: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(top = 4.dp)
    ) {
        Icon(imageVector = icono, contentDescription = null, tint = Color(0xFF64B5F6), modifier = Modifier.size(20.dp))
        Text(titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun TarjetaAccion(
    modifier: Modifier = Modifier,
    icono: ImageVector,
    titulo: String,
    subtitulo: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.85f)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(imageVector = icono, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            Column {
                Text(titulo, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text(subtitulo, color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}

// Lista de reservas genérica con botón cancelar
@Composable
fun TarjetaListaReservas(
    reservas: List<Reserva>,
    todasLasPistas: List<Pista>,
    mensajeVacio: String,
    onCancelar: (Reserva) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (reservas.isEmpty()) {
                Text(mensajeVacio, color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
            } else {
                reservas.forEach { reserva ->
                    val nombrePista = todasLasPistas.firstOrNull { it.id == reserva.pistaId }?.nombre
                        ?: "Pista #${reserva.pistaId}"
                    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF0288D1)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(nombrePista, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Text(
                                "${reserva.fecha}  ·  ${reserva.horaInicio} - ${reserva.horaFin}",
                                color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp
                            )
                        }
                        TextButton(onClick = { onCancelar(reserva) }) {
                            Text("Cancelar", color = Color(0xFFFF6B6B), fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// Reservas de equipo para el JUGADOR — con botones Asisto / No asisto
@Composable
fun TarjetaReservasEquipoJugador(
    reservas: List<Reserva>,
    todasLasPistas: List<Pista>,
    asistencia: String,
    onAsistir: () -> Unit,
    onNoAsistir: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f))
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            if (reservas.isEmpty()) {
                Text("No hay reservas de equipo próximas.", color = Color.White.copy(alpha = 0.6f), fontSize = 14.sp)
            } else {
                reservas.forEach { reserva ->
                    val nombrePista = todasLasPistas.firstOrNull { it.id == reserva.pistaId }?.nombre
                        ?: "Pista #${reserva.pistaId}"
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp)).background(Color(0xFF1565C0)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DateRange, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(nombrePista, color = Color.White, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                            Text(
                                "${reserva.fecha}  ·  ${reserva.horaInicio} - ${reserva.horaFin}",
                                color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp
                            )
                        }
                    }
                }

                HorizontalDivider(color = Color.White.copy(alpha = 0.15f))

                Text("¿Asistes a los entrenamientos?", color = Color.White.copy(alpha = 0.8f), fontSize = 13.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = onAsistir,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (asistencia == "ASISTE") Color(0xFF43A047)
                            else Color(0xFF1A1A1A).copy(alpha = 0.5f)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("✓ Asisto", color = Color.White, fontSize = 13.sp)
                    }
                    Button(
                        onClick = onNoAsistir,
                        modifier = Modifier.weight(1f).height(38.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (asistencia == "NO_ASISTE") Color(0xFFE53935)
                            else Color(0xFF1A1A1A).copy(alpha = 0.5f)
                        ),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text("✗ No asisto", color = Color.White, fontSize = 13.sp)
                    }
                }
                val textoEstado = when (asistencia) {
                    "ASISTE"    -> "✓ Has confirmado tu asistencia"
                    "NO_ASISTE" -> "✗ Has declinado tu asistencia"
                    else        -> "Sin responder todavía"
                }
                val colorEstado = when (asistencia) {
                    "ASISTE"    -> Color(0xFF81C784)
                    "NO_ASISTE" -> Color(0xFFEF9A9A)
                    else        -> Color.White.copy(alpha = 0.4f)
                }
                Text(textoEstado, color = colorEstado, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun TarjetaIncidencias(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFB71C1C).copy(alpha = 0.7f)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            Column {
                Text("Notificar incidencia", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Entrenamiento cancelado u otras incidencias", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun TarjetaLocalizacion(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF00695C).copy(alpha = 0.85f)),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
            Column {
                Text("Ver ubicación del centro", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Text("Jacarilla, Alicante", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}