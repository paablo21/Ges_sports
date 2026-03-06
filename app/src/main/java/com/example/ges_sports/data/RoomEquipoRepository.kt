package com.example.ges_sports.data

import com.example.ges_sports.database.EquipoDao
import com.example.ges_sports.models.Equipo
import com.example.ges_sports.repository.EquipoRepository
import kotlinx.coroutines.flow.Flow

class RoomEquipoRepository(private val equipoDao: EquipoDao) : EquipoRepository {

    override fun getAllEquipos(): Flow<List<Equipo>> = equipoDao.getAll()

    override fun getEquiposByEntrenador(entrenadorId: Int): Flow<List<Equipo>> =
        equipoDao.getByEntrenador(entrenadorId)

    override suspend fun addEquipo(equipo: Equipo): Equipo {
        val id = equipoDao.insert(equipo)
        return equipo.copy(id = id.toInt())
    }

    override suspend fun updateEquipo(equipo: Equipo): Int = equipoDao.update(equipo)

    override suspend fun deleteEquipo(id: Int): Boolean {
        val equipo = equipoDao.getById(id)
        return if (equipo != null) {
            equipoDao.delete(equipo)
            true
        } else false
    }
}