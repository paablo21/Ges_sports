/*package com.example.ges_sports.data

import android.R.attr.text
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.ges_sports.models.User
import com.example.ges_sports.repository.UserRepository
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.io.File

class JsonUserRepository(private val context: Context): UserRepository {

    private val TAG="FicheroJson"

    private val jsonFile = File(context.filesDir, "users.json") //FICHERO DINAMICO

    val jsonString = context.assets.open("users.json")   // FICHERO DE INICIO CARGADO DESDE ASSETS
        .bufferedReader()
        .use { it.readText() }

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
    }
    private val users = mutableListOf<User>()
    private var nextId: Int = 1

    init {
        Log.i(TAG, "Entra en init")
        loadFile()
        //if (jsonFile.exists()) jsonFile.delete()
    }

    private fun loadFile(){

        if (jsonFile.exists()){
            Log.i(TAG, "Encontrado fihero")
            val text = jsonFile.readText()
            if(text.isNotBlank()){
                val loadedUsers: List<User> = json.decodeFromString(text)
                users.clear()
                users.addAll(loadedUsers)
                nextId = (users.maxOfOrNull { it.id } ?: 0) + 1
            }
       }else{
            Log.i(TAG, "Carga desde assets")
            loadFromAssets()
            //saveToFile()
        }
        Log.i(TAG, "CARGADO")
    }
    private fun loadFromAssets() {


        if (jsonString.isEmpty()) {
            users.clear()
            nextId = 1
            return
        }

        if (jsonString.isBlank()) {
            users.clear()
            nextId = 1
            return
        }
        Log.i(TAG, "assets1")
        val loadedUsers: List<User> = json.decodeFromString(jsonString)
        Log.i(TAG, "assets2")
        users.clear()
        users.addAll(loadedUsers)
        Log.i(TAG, "assets3")
        //saveToFile()
        nextId = (users.maxOfOrNull { it.id } ?: 0) + 1
    }

    private fun saveToFile() {
        val text = json.encodeToString(users)
        jsonFile.writeText(text)
    }
    private fun getNewId(): Int {
        return (users.maxOfOrNull { it.id } ?: 0) + 1
    }


    override suspend fun getAllUsers(): List<User> {
        return users.toList()
    }

    override suspend fun getUsersByRole(rol: String): List<User> {
        return users.filter { it.rol == rol }
    }

    override suspend fun getUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    override suspend fun addUser(user: User): User {
        nextId = getNewId()
        val newUser = user.copy(id = nextId)
        users.add(newUser)
        saveToFile()
        return newUser
    }

    override suspend fun updateUser(user: User): Boolean {
        val index = users.indexOfFirst { it.id == user.id }
        if (index == -1) return false
        users[index] = user
        saveToFile()
        return true
    }

    override suspend fun deleteUser(user: User): Boolean {
        TODO("Not yet implemented")
    }

    suspend fun deleteUser(id: Int): Boolean {
        val removed = users.removeIf { it.id == id }
        if (removed) {
            saveToFile()
        }
        return removed
    }
}

/*// ========================
    // Implementación interfaz
    // ========================

    override suspend fun getAllUsers(): List<User> {
        return users.toList() // copia para no exponer la lista interna
    }

    override suspend fun getUsersByRole(role: String): List<User> {
        return users.filter { it.rol == role }
    }

    override suspend fun getUserById(id: Int): User? {
        return users.find { it.id == id }
    }

    override suspend fun addUser(user: User): User {
        val newUser = user.copy(id = nextId++)
        users.add(newUser)
        saveToFile()
        return newUser
    }

    override suspend fun updateUser(user: User): Boolean {
        val index = users.indexOfFirst { it.id == user.id }
        if (index == -1) return false

        users[index] = user
        saveToFile()
        return true
    }

    override suspend fun deleteUser(id: Int): Boolean {
        val removed = users.removeIf { it.id == id }
        if (removed) {
            saveToFile()
        }
        return removed
    }

    */