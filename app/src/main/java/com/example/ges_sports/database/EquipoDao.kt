package com.example.ges_sports.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ges_sports.models.Equipo
import kotlinx.coroutines.flow.Flow

@Dao
interface EquipoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(equipo: Equipo): Long

    @Query("SELECT * FROM equipos ORDER BY nombre ASC")
    fun getAll(): Flow<List<Equipo>>

    @Query("SELECT * FROM equipos WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Equipo?

    @Query("SELECT * FROM equipos WHERE entrenadorId = :entrenadorId")
    fun getByEntrenador(entrenadorId: Int): Flow<List<Equipo>>

    @Update
    suspend fun update(equipo: Equipo): Int

    @Delete
    suspend fun delete(equipo: Equipo)
}