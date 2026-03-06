package com.example.ges_sports.repository

import com.example.ges_sports.models.Pista
import kotlinx.coroutines.flow.Flow

interface PistaRepository {
    fun getAllPistas(): Flow<List<Pista>>
    fun getPistasDisponibles(): Flow<List<Pista>>
    suspend fun addPista(pista: Pista): Pista
    suspend fun updatePista(pista: Pista): Int
    suspend fun deletePista(id: Int): Boolean
}