package com.example.ges_sports.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.ges_sports.models.Pista
import kotlinx.coroutines.flow.Flow

@Dao
interface PistaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pista: Pista): Long

    @Query("SELECT * FROM pistas ORDER BY nombre ASC")
    fun getAll(): Flow<List<Pista>>

    @Query("SELECT * FROM pistas WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): Pista?

    @Query("SELECT * FROM pistas WHERE disponible = 1 ORDER BY nombre ASC")
    fun getDisponibles(): Flow<List<Pista>>

    @Update
    suspend fun update(pista: Pista): Int

    @Delete
    suspend fun delete(pista: Pista)
}