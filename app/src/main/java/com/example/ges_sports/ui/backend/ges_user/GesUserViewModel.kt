package com.example.ges_sports.ui.backend.ges_user

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ges_sports.repository.UserRepository
import com.example.ges_sports.models.User
import kotlinx.coroutines.launch

class GesUserViewModel (val userRepository: UserRepository): ViewModel() {
    private var _users by mutableStateOf<List<User>>(emptyList())
    val users: List<User> get() = _users

    private var _selectedRole by mutableStateOf<String?>(null)
    val selectedRole: String? get() = _selectedRole

    init {
        // CAMBIO: Colectamos el Flow de Room
        viewModelScope.launch {
            userRepository.getAllUsers().collect { lista ->
                _users = lista
            }
        }
    }
    /*init {
        //podemos utilizar directamente loadUsers()
        viewModelScope.launch {
            _users = userRepository.getAllUsers()
        }
    }*/

    /* fun loadUsers(){
         viewModelScope.launch {
             if(_selectedRole==null){
                 _users=userRepository.getAllUsers()
             }else{
                 _users=userRepository.getUsersByRole(_selectedRole!!)
             }
         }
     }
     fun onRoleSelected(rol: String?) {
         _selectedRole = rol
         viewModelScope.launch {
             _users = if (rol == null) {
                 userRepository.getAllUsers()
             } else {
                 userRepository.getUsersByRole(rol)
             }
         }
     }*/
    fun onRoleSelected(rol: String?) {
        _selectedRole = rol
        viewModelScope.launch {
            // 🔧 CAMBIO: también colectamos el Flow filtrado
            val flow = if (rol == null)
                userRepository.getAllUsers()
            else
                userRepository.getUsersByRole(rol)

            flow.collect { lista ->
                _users = lista
            }
        }
    }
    fun addUser(user: User){
        viewModelScope.launch{
            userRepository.addUser(user)
            //  loadUsers()  NO HACE FALTA YA LO HACE FLOW
        }
    }

    fun updateUser(user: User, onResult: ((Boolean) -> Unit)? = null) {
        viewModelScope.launch {
            val rowsUpdated = userRepository.updateUser(user)
            onResult?.invoke(rowsUpdated > 0)
            // 🔥 No hace falta tocar _users: Flow se encarga solo
        }
    }

     fun deleteUser(user: User) {
         viewModelScope.launch {
             userRepository.deleteUser(user.id)
             // 🔧 Igual: Flow actualiza automáticamente _users
         }
     }
}