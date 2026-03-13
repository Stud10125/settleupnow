package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.model.User
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class ProfileViewModel : ViewModel() {
    private val auth = Firebase.auth
    private val db = Firebase.database.reference

    private val _user = MutableStateFlow<User?>(
        auth.currentUser?.let { 
            User(it.uid, it.displayName ?: "User", it.email ?: "") 
        }
    )
    val user: StateFlow<User?> = _user.asStateFlow()

    init {
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        val currentUser = auth.currentUser ?: return
        
        viewModelScope.launch {
            try {
                val snapshot = db.child("users").child(currentUser.uid).get().await()
                if (snapshot.exists()) {
                    val name = snapshot.child("name").value as? String ?: currentUser.displayName ?: "User"
                    val email = snapshot.child("email").value as? String ?: currentUser.email ?: ""
                    _user.value = User(currentUser.uid, name, email)
                }
            } catch (e: Exception) {

            }
        }
    }

    fun logout(onSuccess: () -> Unit) {
        auth.signOut()
        onSuccess()
    }
}
