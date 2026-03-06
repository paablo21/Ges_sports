package com.example.ges_sports.ui.backend.ges_user

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ges_sports.repository.UserRepository
import com.example.ges_sports.models.User
import kotlinx.coroutines.launch

class GesUserViewModel(val userRepository: UserRepository) : ViewModel() {

    private var _users by mutableStateOf<List<User>>(emptyList())
    val users: List<User> get() = _users

    private var _selectedRole by mutableStateOf<String?>(null)
    val selectedRole: String? get() = _selectedRole

    init {
        viewModelScope.launch {
            userRepository.getAllUsers().collect { lista ->
                _users = lista
            }
        }
    }

    fun onRoleSelected(rol: String?) {
        _selectedRole = rol
        viewModelScope.launch {
            val flow = if (rol == null)
                userRepository.getAllUsers()
            else
                userRepository.getUsersByRole(rol)

            flow.collect { lista ->
                _users = lista
            }
        }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            userRepository.addUser(user)
        }
    }

    fun updateUser(user: User, onResult: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            val rowsUpdated = userRepository.updateUser(user)
            onResult?.invoke(rowsUpdated > 0)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            userRepository.deleteUser(user.id)
        }
    }
}