package com.example.ges_sports.domain
import com.example.ges_sports.models.User
import com.example.ges_sports.repository.UserRepository
import kotlinx.coroutines.flow.first

class LogicLogin(
    private val userRepository: UserRepository
) {

    suspend fun comprobarLogin(email: String, password: String): User {
        if (email.isBlank() || password.isBlank()) {
            throw IllegalArgumentException("Los campos no pueden estar vacíos.")
        }

        val users = userRepository.getAllUsers().first()

        return users.find {
            it.email.equals(email, ignoreCase = true) &&
                    it.password == password
        } ?: throw IllegalArgumentException("Email o contraseña incorrectos.")
    }
}
