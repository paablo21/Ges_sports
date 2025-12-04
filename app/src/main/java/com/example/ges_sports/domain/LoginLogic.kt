package com.example.ges_sports.domain

import com.example.ges_sports.data.DataUserRepository
import com.example.ges_sports.models.User

class LogicLogin {

    fun comprobarLogin(email: String, password: String): User {

        // Usamos la lista real de usuarios del CRUD
        val usuarios = DataUserRepository.getUsuariosNoSuspend()

        // Buscar el usuario por email y contraseña
        val user = usuarios.firstOrNull { usuario ->
            usuario.email.equals(email, ignoreCase = true) &&
                    usuario.password == password
        }

        if (user == null) {
            throw IllegalArgumentException("Credenciales incorrectas")
        }

        return user
    }
}
