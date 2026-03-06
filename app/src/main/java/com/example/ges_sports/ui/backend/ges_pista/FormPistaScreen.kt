package com.example.ges_sports.ui.backend.ges_pista

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.ges_sports.models.Pista

private val tiposPista = listOf("Pádel", "Tenis", "Fútbol", "Baloncesto")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPistaScreen(
    navController: NavHostController,
    viewModel: GesPistaViewModel,
    pistaId: Int
) {
    val pistaEditando = viewModel.pistas.firstOrNull { it.id == pistaId }

    var nombre by rememberSaveable { mutableStateOf(pistaEditando?.nombre ?: "") }
    var tipo by rememberSaveable { mutableStateOf(pistaEditando?.tipo ?: "Pádel") }
    var disponible by rememberSaveable { mutableStateOf(pistaEditando?.disponible ?: true) }
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
                        if (pistaEditando == null) "Nueva pista" else "Editar pista",
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
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // NOMBRE
            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre de la pista", color = Color.White) },
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

            // TIPO
            Text("Tipo de pista", color = Color.White)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                tiposPista.chunked(3).forEach { fila ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        fila.forEach { t ->
                            FilterChip(
                                selected = tipo == t,
                                onClick = { tipo = t },
                                label = { Text(t) },
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

            // DISPONIBLE
            Text("Disponibilidad", color = Color.White)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Switch(
                    checked = disponible,
                    onCheckedChange = { disponible = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF43A047),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFE53935)
                    )
                )
                Text(
                    text = if (disponible) "Disponible" else "No disponible",
                    color = Color.White
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(errorMessage, color = Color(0xFFFF6B6B))
            }

            Spacer(Modifier.height(8.dp))

            // BOTÓN GUARDAR
            Button(
                onClick = {
                    if (nombre.isBlank()) {
                        errorMessage = "El nombre es obligatorio"
                        return@Button
                    }
                    if (pistaEditando == null) {
                        viewModel.addPista(
                            Pista(id = 0, nombre = nombre, tipo = tipo, disponible = disponible)
                        )
                    } else {
                        viewModel.updatePista(
                            Pista(id = pistaEditando.id, nombre = nombre, tipo = tipo, disponible = disponible)
                        )
                    }
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth().height(55.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F5AA9),
                    contentColor = Color.White
                )
            ) {
                Text(if (pistaEditando == null) "Crear pista" else "Guardar cambios")
            }
        }
    }
}