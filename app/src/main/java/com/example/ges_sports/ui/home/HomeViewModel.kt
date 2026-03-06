package com.example.ges_sports.ui.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ges_sports.models.Equipo
import com.example.ges_sports.models.Pista
import com.example.ges_sports.models.Reserva
import com.example.ges_sports.models.User
import com.example.ges_sports.repository.EquipoRepository
import com.example.ges_sports.repository.PistaRepository
import com.example.ges_sports.repository.ReservaRepository
import com.example.ges_sports.repository.UserRepository
import kotlinx.coroutines.launch

class HomeViewModel(
    val reservaRepository: ReservaRepository,
    val pistaRepository: PistaRepository,
    val equipoRepository: EquipoRepository,
    val userRepository: UserRepository
) : ViewModel() {

    private var _reservas by mutableStateOf<List<Reserva>>(emptyList())
    val reservas: List<Reserva> get() = _reservas

    private var _pistasDisponibles by mutableStateOf<List<Pista>>(emptyList())
    val pistasDisponibles: List<Pista> get() = _pistasDisponibles

    private var _todasLasPistas by mutableStateOf<List<Pista>>(emptyList())
    val todasLasPistas: List<Pista> get() = _todasLasPistas

    private var _equipos by mutableStateOf<List<Equipo>>(emptyList())
    val equipos: List<Equipo> get() = _equipos

    private var _usuarios by mutableStateOf<List<User>>(emptyList())
    val usuarios: List<User> get() = _usuarios

    init {
        viewModelScope.launch { reservaRepository.getAllReservas().collect { _reservas = it } }
        viewModelScope.launch { pistaRepository.getPistasDisponibles().collect { _pistasDisponibles = it } }
        viewModelScope.launch { pistaRepository.getAllPistas().collect { _todasLasPistas = it } }
        viewModelScope.launch { equipoRepository.getAllEquipos().collect { _equipos = it } }
        viewModelScope.launch { userRepository.getAllUsers().collect { _usuarios = it } }
    }

    // Reservas personales del jugador
    fun reservasDeUsuario(usuarioId: Int): List<Reserva> =
        _reservas.filter { it.usuarioId == usuarioId && it.equipoId == -1 }

    // Reservas del equipo al que pertenece el jugador
    fun reservasDeEquipo(equipoId: Int): List<Reserva> =
        _reservas.filter { it.equipoId == equipoId }

    // Equipo del jugador
    fun equipoDeJugador(usuarioId: Int): Equipo? {
        val user = _usuarios.firstOrNull { it.id == usuarioId } ?: return null
        return if (user.equipoId != -1) _equipos.firstOrNull { it.id == user.equipoId } else null
    }

    // Equipos del entrenador
    fun equiposDeEntrenador(entrenadorId: Int): List<Equipo> =
        _equipos.filter { it.entrenadorId == entrenadorId }

    // Estado de asistencia del jugador (leído de su propio campo)
    fun asistenciaDeJugador(usuarioId: Int): String {
        return _usuarios.firstOrNull { it.id == usuarioId }?.asistencia ?: "PENDIENTE"
    }

    fun addReserva(reserva: Reserva) {
        viewModelScope.launch { reservaRepository.addReserva(reserva) }
    }

    fun deleteReserva(reserva: Reserva) {
        viewModelScope.launch { reservaRepository.deleteReserva(reserva.id) }
    }

    // Actualizar asistencia del jugador en su propio User
    fun actualizarAsistencia(usuarioId: Int, asistencia: String) {
        viewModelScope.launch {
            val user = _usuarios.firstOrNull { it.id == usuarioId } ?: return@launch
            userRepository.updateUser(user.copy(asistencia = asistencia))
        }
    }
}