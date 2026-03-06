package com.example.ges_sports.ui.backend.ges_reserva

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

class GesReservaViewModel(
    val reservaRepository: ReservaRepository,
    val pistaRepository: PistaRepository,
    val userRepository: UserRepository,
    val equipoRepository: EquipoRepository
) : ViewModel() {

    private var _reservas by mutableStateOf<List<Reserva>>(emptyList())
    val reservas: List<Reserva> get() = _reservas

    private var _pistas by mutableStateOf<List<Pista>>(emptyList())
    val pistas: List<Pista> get() = _pistas

    private var _usuarios by mutableStateOf<List<User>>(emptyList())
    val usuarios: List<User> get() = _usuarios

    private var _equipos by mutableStateOf<List<Equipo>>(emptyList())
    val equipos: List<Equipo> get() = _equipos

    init {
        viewModelScope.launch {
            reservaRepository.getAllReservas().collect { _reservas = it }
        }
        viewModelScope.launch {
            pistaRepository.getAllPistas().collect { _pistas = it }
        }
        viewModelScope.launch {
            userRepository.getAllUsers().collect { _usuarios = it }
        }
        viewModelScope.launch {
            equipoRepository.getAllEquipos().collect { _equipos = it }
        }
    }

    fun addReserva(reserva: Reserva) {
        viewModelScope.launch { reservaRepository.addReserva(reserva) }
    }

    fun updateReserva(reserva: Reserva) {
        viewModelScope.launch { reservaRepository.updateReserva(reserva) }
    }

    fun deleteReserva(reserva: Reserva) {
        viewModelScope.launch { reservaRepository.deleteReserva(reserva.id) }
    }
}