package com.example.ges_sports.repository

import com.example.ges_sports.models.Equipo
import kotlinx.coroutines.flow.Flow

interface EquipoRepository {
    fun getAllEquipos(): Flow<List<Equipo>>
    fun getEquiposByEntrenador(entrenadorId: Int): Flow<List<Equipo>>
    suspend fun addEquipo(equipo: Equipo): Equipo
    suspend fun updateEquipo(equipo: Equipo): Int
    suspend fun deleteEquipo(id: Int): Boolean
}