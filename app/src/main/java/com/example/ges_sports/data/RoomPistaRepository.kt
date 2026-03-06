package com.example.ges_sports.data

import com.example.ges_sports.database.PistaDao
import com.example.ges_sports.models.Pista
import com.example.ges_sports.repository.PistaRepository
import kotlinx.coroutines.flow.Flow

class RoomPistaRepository(private val pistaDao: PistaDao) : PistaRepository {

    override fun getAllPistas(): Flow<List<Pista>> = pistaDao.getAll()

    override fun getPistasDisponibles(): Flow<List<Pista>> = pistaDao.getDisponibles()

    override suspend fun addPista(pista: Pista): Pista {
        val id = pistaDao.insert(pista)
        return pista.copy(id = id.toInt())
    }

    override suspend fun updatePista(pista: Pista): Int = pistaDao.update(pista)

    override suspend fun deletePista(id: Int): Boolean {
        val pista = pistaDao.getById(id)
        return if (pista != null) {
            pistaDao.delete(pista)
            true
        } else false
    }
}