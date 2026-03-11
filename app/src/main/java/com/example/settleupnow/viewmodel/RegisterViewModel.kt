package com.example.settleupnow.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    var name = mutableStateOf("")
    var email = mutableStateOf("")
    var password = mutableStateOf("")

    fun signUp() {
        val userName = name.value
        val userEmail = email.value
        val userPassword = password.value
    }
}
