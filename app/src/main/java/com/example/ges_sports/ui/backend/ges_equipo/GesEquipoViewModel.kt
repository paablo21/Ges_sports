package com.example.ges_sports.ui.backend.ges_equipo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ges_sports.models.Equipo
import com.example.ges_sports.models.User
import com.example.ges_sports.repository.EquipoRepository
import com.example.ges_sports.repository.UserRepository
import kotlinx.coroutines.launch

class GesEquipoViewModel(
    val equipoRepository: EquipoRepository,
    val userRepository: UserRepository
) : ViewModel() {

    private var _equipos by mutableStateOf<List<Equipo>>(emptyList())
    val equipos: List<Equipo> get() = _equipos

    private var _entrenadores by mutableStateOf<List<User>>(emptyList())
    val entrenadores: List<User> get() = _entrenadores

    private var _todosUsuarios by mutableStateOf<List<User>>(emptyList())

    // Jugadores sin equipo, o que ya pertenecen al equipo que se está editando
    fun jugadoresDisponibles(equipoId: Int): List<User> =
        _todosUsuarios.filter {
            it.rol == "JUGADOR" && (it.equipoId == -1 || it.equipoId == equipoId)
        }

    // Jugadores que pertenecen a un equipo concreto
    fun jugadoresDeEquipo(equipoId: Int): List<User> =
        _todosUsuarios.filter { it.equipoId == equipoId }

    init {
        viewModelScope.launch {
            equipoRepository.getAllEquipos().collect { _equipos = it }
        }
        viewModelScope.launch {
            userRepository.getUsersByRole("ENTRENADOR").collect { _entrenadores = it }
        }
        viewModelScope.launch {
            userRepository.getAllUsers().collect { _todosUsuarios = it }
        }
    }

    // Crea el equipo y asigna los jugadores usando el ID que devuelve Room
    fun addEquipo(equipo: Equipo, jugadoresSeleccionados: List<User>) {
        viewModelScope.launch {
            val equipoCreado = equipoRepository.addEquipo(equipo)
            jugadoresSeleccionados.forEach { jugador ->
                userRepository.updateUser(jugador.copy(equipoId = equipoCreado.id))
            }
        }
    }

    fun updateEquipo(equipo: Equipo) {
        viewModelScope.launch { equipoRepository.updateEquipo(equipo) }
    }

    fun deleteEquipo(equipo: Equipo) {
        viewModelScope.launch { equipoRepository.deleteEquipo(equipo.id) }
    }

    // Al editar: desasigna jugadores eliminados y asigna los nuevos
    fun asignarJugadores(
        equipoId: Int,
        seleccionados: List<User>,
        anteriores: List<User>
    ) {
        viewModelScope.launch {
            anteriores
                .filter { ant -> seleccionados.none { it.id == ant.id } }
                .forEach { userRepository.updateUser(it.copy(equipoId = -1)) }

            seleccionados.forEach { jugador ->
                userRepository.updateUser(jugador.copy(equipoId = equipoId))
            }
        }
    }
}