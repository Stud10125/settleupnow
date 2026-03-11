package com.example.settleupnow.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel(){

    var email by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    fun onEmailChanged(value: String) { email = value }
    fun onNewPasswordChanged(value: String) { newPassword = value }
    fun onConfirmPasswordChanged(value: String) { confirmPassword = value }

    fun changePassword() {
        if (newPassword == confirmPassword) {
            Log.d("ChangePassword", "Password changed for $email")
        } else {
            Log.d("ChangePassword", "Passwords do not match")
        }
    }

    fun onAccountClick() {
    }

    fun onChangePasswordClick() {
    }

    fun onSupportClick() {
    }

    fun onAboutClick() {
    }
}