package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.settleupnow.model.User
import kotlinx.coroutines.launch

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
        if (!_members.value.all { it.userId != user.userId }) return // Check by ID
        _members.value = _members.value + user
    }

    fun removeMember(user: User) {
        _members.value = _members.value.filter { it.userId != user.userId }
    }

    fun addMemberByEmail(email: String, onResult: (Boolean, String) -> Unit) {
        val db = com.google.firebase.database.FirebaseDatabase.getInstance().reference
        db.child("users").orderByChild("email").equalTo(email).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val userSnapshot = snapshot.children.first()
                    val uid = userSnapshot.key ?: return@addOnSuccessListener
                    val name = userSnapshot.child("name").value as? String ?: ""
                    val user = User(uid, name, email)
                    addMember(user)
                    onResult(true, "Member added")
                } else {
                    onResult(false, "User not found")
                }
            }
            .addOnFailureListener {
                onResult(false, it.message ?: "Error")
            }
    }

    fun createGroup(onResult: (Boolean, String) -> Unit) {
        if (_groupName.value.isBlank()) {
            onResult(false, "Group name cannot be empty")
            return
        }
        
        repository.createGroup(
            name = _groupName.value,
            description = _description.value,
            members = _members.value,
            onResult = onResult
        )
    }
}
