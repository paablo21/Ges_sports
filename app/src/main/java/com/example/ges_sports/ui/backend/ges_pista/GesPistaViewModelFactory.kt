package com.example.ges_sports.ui.backend.ges_pista

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ges_sports.data.RoomPistaRepository
import com.example.ges_sports.database.AppDatabase

class GesPistaViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(context)
        val repo = RoomPistaRepository(db.pistaDao())
        @Suppress("UNCHECKED_CAST")
        return GesPistaViewModel(repo) as T
    }
}