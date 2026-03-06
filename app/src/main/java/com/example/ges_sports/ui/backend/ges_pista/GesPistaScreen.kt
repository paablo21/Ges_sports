package com.example.ges_sports.ui.backend.ges_pista

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
import com.example.ges_sports.models.Pista
import com.example.ges_sports.ui.components.AppTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GesPistaScreen(
    navController: NavHostController,
    viewModel: GesPistaViewModel
) {
    val pistas = viewModel.pistas

    Scaffold(
        topBar = { AppTopBar("PISTAS") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("formpista/-1") },
                containerColor = Color(0xFF64B5F6),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir pista")
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
            if (pistas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay pistas creadas", color = Color.White.copy(alpha = 0.6f))
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(pistas) { pista ->
                        PistaCard(
                            pista = pista,
                            onEdit = { navController.navigate("formpista/${pista.id}") },
                            onDelete = { viewModel.deletePista(pista) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PistaCard(pista: Pista, onEdit: () -> Unit, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF0288D1)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = pista.nombre.first().uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(pista.nombre, color = Color(0xFF01579B), fontWeight = FontWeight.Bold)
                Text(pista.tipo, color = Color.DarkGray, style = MaterialTheme.typography.bodySmall)
                Spacer(Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (pista.disponible) Color(0xFF43A047) else Color(0xFFE53935))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (pista.disponible) "Disponible" else "No disponible",
                        color = Color.White,
                        fontSize = 11.sp
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