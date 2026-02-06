package com.example.ges_sports.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White
            )
        },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Star,   // ⭐ icono
                contentDescription = null,
                tint = Color(0xFFFFC107),            // 🟡 amarillo
                modifier = Modifier.padding(start = 12.dp)
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Black
        )
    )
}