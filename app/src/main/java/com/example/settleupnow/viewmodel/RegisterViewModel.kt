package com.example.settleupnow.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository

class RegisterViewModel : ViewModel() {
    private val repo = FirebaseRepository()

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")

    fun signUp(onResult: (Boolean, String) -> Unit) {
        val userName = name.value
        val userEmail = email.value
        val userPassword = password.value

        registerUser(userEmail, userPassword, userName, onResult)
    }

    fun registerUser(
        email: String,
        password: String,
        name: String,
        onResult: (Boolean, String) -> Unit
    ) {
        repo.register(email, password, name, onResult)
    }
}
