package com.example.ges_sports.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pistas")
data class Pista(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val tipo: String,
    val disponible: Boolean = true
)