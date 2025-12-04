package com.example.ges_sports.ui.backend.ges_user

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ges_sports.models.User
import com.example.ges_sports.repository.UserRepository
import kotlinx.coroutines.launch

class GesUserViewModel(
    private val repositorio: UserRepository
) : ViewModel() {

    private var listaCompleta: List<User> = emptyList()

    var usuarios by mutableStateOf<List<User>>(emptyList())
        private set

    var filtroRol by mutableStateOf<String?>(null)
        private set

    init {
        cargarUsuarios()
    }

    // AHORA ES PÚBLICA PARA PODER FORZAR RECARGA AL VOLVER DE FORMUSER
    fun cargarUsuarios() {
        viewModelScope.launch {
            listaCompleta = repositorio.getAllUsers()
            aplicarFiltros()
        }
    }

    private fun aplicarFiltros() {
        var lista = listaCompleta

        filtroRol?.let { rol ->
            lista = lista.filter { it.rol == rol }
        }


        usuarios = lista
    }


    fun seleccionarRol(rol: String?) {
        filtroRol = rol
        aplicarFiltros()
    }

    fun crearUsuario(nombre: String, email: String, password: String, rol: String) {
        viewModelScope.launch {
            if (nombre.isBlank() || email.isBlank() || password.isBlank()) return@launch
            repositorio.addUser(User(0, nombre, email, password, rol))
            cargarUsuarios()
        }
    }

    fun editarUsuario(usuario: User) {
        viewModelScope.launch {
            repositorio.updateUser(usuario)
            cargarUsuarios()
        }
    }

    fun borrarUsuario(usuario: User) {
        viewModelScope.launch {
            repositorio.deleteUser(usuario)
            cargarUsuarios()
        }
    }
}
