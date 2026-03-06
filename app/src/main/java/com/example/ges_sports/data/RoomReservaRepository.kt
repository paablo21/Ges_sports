package com.example.ges_sports.data

import com.example.ges_sports.database.ReservaDao
import com.example.ges_sports.models.Reserva
import com.example.ges_sports.repository.ReservaRepository
import kotlinx.coroutines.flow.Flow

class RoomReservaRepository(private val reservaDao: ReservaDao) : ReservaRepository {

    override fun getAllReservas(): Flow<List<Reserva>> = reservaDao.getAll()

    override fun getReservasByUsuario(usuarioId: Int): Flow<List<Reserva>> =
        reservaDao.getByUsuario(usuarioId)

    override fun getReservasByEquipo(equipoId: Int): Flow<List<Reserva>> =
        reservaDao.getByEquipo(equipoId)

    override suspend fun addReserva(reserva: Reserva): Reserva {
        val id = reservaDao.insert(reserva)
        return reserva.copy(id = id.toInt())
    }

    override suspend fun updateReserva(reserva: Reserva): Int = reservaDao.update(reserva)

    override suspend fun deleteReserva(id: Int): Boolean {
        val reserva = reservaDao.getById(id)
        return if (reserva != null) {
            reservaDao.delete(reserva)
            true
        } else false
    }
}