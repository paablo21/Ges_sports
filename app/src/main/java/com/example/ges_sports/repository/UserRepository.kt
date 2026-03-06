package com.example.ges_sports.repository

import com.example.ges_sports.models.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    fun getAllUsers(): Flow<List<User>>

    fun getUsersByRole(role: String): Flow<List<User>>

    suspend fun addUser(user: User): User

    suspend fun updateUser(user: User): Int

    suspend fun deleteUser(id: Int): Boolean
}