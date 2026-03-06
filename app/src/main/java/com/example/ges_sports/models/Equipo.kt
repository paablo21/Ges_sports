package com.example.ges_sports.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "equipos")
data class Equipo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val deporte: String,
    val entrenadorId: Int       // guardamos el id del usuario con rol ENTRENADOR
)