package com.example.ges_sports.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ges_sports.models.Reserva
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(reserva: Reserva): Long

    // Todas las reservas
    @Query("SELECT * FROM reservas ORDER BY fecha ASC, horaInicio ASC")
    fun getAll(): Flow<List<Reserva>>

    // Reservas de un usuario concreto (reservas personales)
    @Query("SELECT * FROM reservas WHERE usuarioId = :usuarioId ORDER BY fecha ASC, horaInicio ASC")
    fun getByUsuario(usuarioId: Int): Flow<List<Reserva>>

    // Reservas de un equipo concreto
    @Query("SELECT * FROM reservas WHERE equipoId = :equipoId ORDER BY fecha ASC, horaInicio ASC")
    fun getByEquipo(equipoId: Int): Flow<List<Reserva>>

    @Query("SELECT * FROM reservas WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Reserva?

    @Update
    suspend fun update(reserva: Reserva): Int

    @Delete
    suspend fun delete(reserva: Reserva)
}