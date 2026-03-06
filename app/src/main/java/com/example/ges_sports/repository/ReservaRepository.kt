package com.example.ges_sports.repository

import com.example.ges_sports.models.Reserva
import kotlinx.coroutines.flow.Flow

interface ReservaRepository {
    fun getAllReservas(): Flow<List<Reserva>>
    fun getReservasByUsuario(usuarioId: Int): Flow<List<Reserva>>
    fun getReservasByEquipo(equipoId: Int): Flow<List<Reserva>>
    suspend fun addReserva(reserva: Reserva): Reserva
    suspend fun updateReserva(reserva: Reserva): Int
    suspend fun deleteReserva(id: Int): Boolean
}