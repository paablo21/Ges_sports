package com.example.ges_sports.repository

import com.example.ges_sports.models.User

interface UserRepository{

    suspend fun getAllUsers(): List<User>

    suspend fun getUsersByRole(rol: String): List<User>

    suspend fun getUserById(id: Int): User?

    suspend fun addUser(user: User): User

    suspend fun updateUser(user: User): Boolean

    suspend fun deleteUser(user: User): Boolean
}




