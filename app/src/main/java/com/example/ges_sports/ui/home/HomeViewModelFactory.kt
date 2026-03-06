package com.example.ges_sports.ui.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ges_sports.data.RoomEquipoRepository
import com.example.ges_sports.data.RoomPistaRepository
import com.example.ges_sports.data.RoomReservaRepository
import com.example.ges_sports.data.RoomUserRepository
import com.example.ges_sports.database.AppDatabase

class HomeViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(context)
        val reservaRepo = RoomReservaRepository(db.reservaDao())
        val pistaRepo = RoomPistaRepository(db.pistaDao())
        val equipoRepo = RoomEquipoRepository(db.equipoDao())
        val userRepo = RoomUserRepository(db.userDao())
        @Suppress("UNCHECKED_CAST")
        return HomeViewModel(reservaRepo, pistaRepo, equipoRepo, userRepo) as T
    }
}