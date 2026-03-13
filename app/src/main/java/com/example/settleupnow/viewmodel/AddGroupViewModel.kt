package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.settleupnow.model.User
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AddGroupViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName.asStateFlow()

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description.asStateFlow()

    private val _members = MutableStateFlow<List<User>>(emptyList())
    val members: StateFlow<List<User>> = _members.asStateFlow()

    fun onGroupNameChange(newName: String) {
        _groupName.value = newName
    }

    fun onDescriptionChange(newDescription: String) {
        _description.value = newDescription
    }

    fun addMember(user: User) {
        if (!_members.value.all { it.userId != user.userId }) return
        _members.value = _members.value + user
    }

    fun removeMember(user: User) {
        _members.value = _members.value.filter { it.userId != user.userId }
    }

    fun clearData() {
        _groupName.value = ""
        _description.value = ""
        _members.value = emptyList()
    }

    fun addMemberByEmail(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val db = com.google.firebase.database.FirebaseDatabase.getInstance().reference
                val snapshot = db.child("users").orderByChild("email").equalTo(email).get().await()
                
                if (snapshot.exists()) {
                    val userSnapshot = snapshot.children.first()
                    val uid = userSnapshot.key ?: throw Exception("User key missing")
                    val name = userSnapshot.child("name").value as? String ?: ""
                    val user = User(uid, name, email)
                    addMember(user)
                    onResult(true, "Member added")
                } else {
                    onResult(false, "User not found")
                }
            } catch (e: Exception) {
                onResult(false, e.message ?: "Error")
            }
        }
    }

    fun createGroup(onResult: (Boolean, String) -> Unit) {
        if (_groupName.value.isBlank()) {
            onResult(false, "Group name cannot be empty")
            return
        }
        
        viewModelScope.launch {
            val (success, message) = repository.createGroup(
                name = _groupName.value,
                description = _description.value,
                members = _members.value
            )
            if (success) {
                clearData()
            }
            onResult(success, message)
        }
    }
}
