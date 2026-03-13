package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    private val auth = Firebase.auth
    
    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email.asStateFlow()

    private val _newPassword = MutableStateFlow("")
    val newPassword: StateFlow<String> = _newPassword.asStateFlow()

    private val _confirmPassword = MutableStateFlow("")
    val confirmPassword: StateFlow<String> = _confirmPassword.asStateFlow()

    fun onEmailChange(newValue: String) {
        _email.value = newValue
    }

    fun onNewPasswordChange(newValue: String) {
        _newPassword.value = newValue
    }

    fun onConfirmPasswordChange(newValue: String) {
        _confirmPassword.value = newValue
    }

    fun changePassword(onResult: (Boolean, String) -> Unit) {
        val newPass = _newPassword.value
        val confirmPass = _confirmPassword.value

        if (newPass.isEmpty()) {
            onResult(false, "Password cannot be empty")
            return
        }

        if (newPass != confirmPass) {
            onResult(false, "Passwords do not match")
            return
        }

        viewModelScope.launch {
            try {
                auth.currentUser?.updatePassword(newPass)?.await()
                onResult(true, "Password updated successfully")
            } catch (e: Exception) {
                onResult(false, e.message ?: "Failed to update password")
            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}
