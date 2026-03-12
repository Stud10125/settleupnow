package com.example.settleupnow.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class LoginViewModel : ViewModel() {

    private val repo = FirebaseRepository()
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email
    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
    }

    fun loginUser(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        repo.login(email, password) { success, message ->
            if (success) {
                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null) {
                    repo.saveUserToDatabase(uid, email)
                }
            }
            onResult(success, message)
        }
    }

}
