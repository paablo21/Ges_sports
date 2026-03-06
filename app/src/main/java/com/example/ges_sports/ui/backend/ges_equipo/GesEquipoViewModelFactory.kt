package com.example.ges_sports.ui.backend.ges_equipo

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ges_sports.data.RoomEquipoRepository
import com.example.ges_sports.data.RoomUserRepository
import com.example.ges_sports.database.AppDatabase
import com.example.ges_sports.database.EquipoDao


class GesEquipoViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(context)
        val equipoRepo = RoomEquipoRepository(db.equipoDao())
        val userRepo = RoomUserRepository(db.userDao())
        @Suppress("UNCHECKED_CAST")
        return GesEquipoViewModel(equipoRepo, userRepo) as T
    }
}