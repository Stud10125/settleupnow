package com.example.settleupnow.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.settleupnow.model.User

class AddGroupViewModel : ViewModel() {
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
        if (!_members.value.contains(user)) {
            _members.value = _members.value + user
        }
    }

    fun removeMember(user: User) {
        _members.value = _members.value - user
    }

    // 🔍 Add member by email lookup
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

    // ✅ Save group into Firebase
    fun createGroup(onResult: (Boolean, String) -> Unit = { _, _ -> }) {
        val db = com.google.firebase.database.FirebaseDatabase.getInstance().reference
        val groupId = db.child("groups").push().key ?: return
        val groupData = mapOf(
            "name" to _groupName.value,
            "description" to _description.value,
            "members" to _members.value.map { mapOf("uid" to it.userId, "name" to it.name, "email" to it.email) }
        )
        db.child("groups").child(groupId).setValue(groupData)
            .addOnSuccessListener { onResult(true, "Group created") }
            .addOnFailureListener { onResult(false, it.message ?: "Error creating group") }
    }
}
