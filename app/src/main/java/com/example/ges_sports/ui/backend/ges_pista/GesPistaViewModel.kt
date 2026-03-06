package com.example.ges_sports.ui.backend.ges_pista

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ges_sports.models.Pista
import com.example.ges_sports.repository.PistaRepository
import kotlinx.coroutines.launch

class GesPistaViewModel(val pistaRepository: PistaRepository) : ViewModel() {

    private var _pistas by mutableStateOf<List<Pista>>(emptyList())
    val pistas: List<Pista> get() = _pistas

    init {
        viewModelScope.launch {
            pistaRepository.getAllPistas().collect { lista ->
                _pistas = lista
            }
        }
    }

    fun addPista(pista: Pista) {
        viewModelScope.launch { pistaRepository.addPista(pista) }
    }

    fun updatePista(pista: Pista) {
        viewModelScope.launch { pistaRepository.updatePista(pista) }
    }

    fun deletePista(pista: Pista) {
        viewModelScope.launch { pistaRepository.deletePista(pista.id) }
    }
}