package com.example.ges_sports.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservas")
data class Reserva(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val pistaId: Int,           // id de la pista reservada
    val usuarioId: Int,         // id del usuario que reserva
    val equipoId: Int = -1,     // id del equipo (-1 si es reserva personal)
    val fecha: String,          // formato "dd/MM/yyyy"
    val horaInicio: String,     // formato "HH:mm"
    val horaFin: String         // formato "HH:mm"
)