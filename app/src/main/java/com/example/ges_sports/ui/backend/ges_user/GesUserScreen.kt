package com.example.ges_sports.ui.backend.ges_user

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController

import com.example.ges_sports.data.RoomUserRepository
import com.example.ges_sports.database.AppDatabase
import com.example.ges_sports.models.UserRoles
import com.example.ges_sports.ui.components.AppTopBar


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GesUserScreen(
    navController: NavHostController,
    viewModel: GesUserViewModel
) {

    val users = viewModel.users
    val selectedRole = viewModel.selectedRole

    Scaffold(
        topBar = {
            AppTopBar("USUARIOS")
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("formuser/-1") },
                containerColor = Color(0xFF64B5F6),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir usuario")
            }
        },
        floatingActionButtonPosition = FabPosition.End
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
                .padding(16.dp)
                .fillMaxSize()
        ) {

            // ───── FILTROS (IGUALES) ─────
            @OptIn(ExperimentalLayoutApi::class)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

                FilterChip(
                    selected = selectedRole == null,
                    onClick = { viewModel.onRoleSelected(null) },
                    label = { Text("TODOS") },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFF1A1A1A).copy(alpha = 0.6f),
                        selectedContainerColor = Color.White,
                        labelColor = Color.White,
                        selectedLabelColor = Color.Black
                    ),
                    border = null
                )

                UserRoles.allRoles.forEach { (roleKey, roleLabel) ->
                    FilterChip(
                        selected = selectedRole == roleKey,
                        onClick = {
                            val newRole = if (selectedRole == roleKey) null else roleKey
                            viewModel.onRoleSelected(newRole)
                        },
                        label = { Text(roleLabel) },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color(0xFF1A1A1A).copy(alpha = 0.6f),
                            selectedContainerColor = Color.White,
                            labelColor = Color.White,
                            selectedLabelColor = Color.Black
                        ),
                        border = null
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            // ───── LISTADO ─────
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(users) { user ->
                    UsuarioCard(
                        nombreRol = user.nombre,
                        onEdit = {
                            navController.navigate("formuser/${user.id}")
                        },
                        onDelete = {
                            viewModel.deleteUser(user)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun UsuarioCard(
    nombreRol: String,
    onEdit: () -> Unit,
    onDelete: () -> Unit
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
            modifier = Modifier.padding(16.dp),
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
                    text = nombreRol.first().uppercase(),
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Spacer(Modifier.width(14.dp))

            Text(
                text = nombreRol,
                modifier = Modifier.weight(1f),
                color = Color(0xFF01579B),
                fontWeight = FontWeight.Bold
            )

            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Editar",
                    tint = Color(0xFF0288D1)
                )
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.Red
                )
            }
        }
    }
}
