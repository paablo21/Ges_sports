package com.example.ges_sports.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuarios")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val email: String,
    val password: String,
    val rol: String,
    val equipoId: Int = -1,                    // -1 = sin equipo
    val asistencia: String = "PENDIENTE"       // PENDIENTE / ASISTE / NO_ASISTE
)