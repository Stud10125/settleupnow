package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.model.User
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupInfoViewModel : ViewModel() {
    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName.asStateFlow()

    private val _members = MutableStateFlow<List<User>>(emptyList())
    val members: StateFlow<List<User>> = _members.asStateFlow()


    init {
        // Dummy data
        _groupName.value = "Roommates"
        _members.value = listOf(
            User("1", "Member 1", "member1@example.com"),
            User("2", "Member 2", "member2@example.com"),
            User("3", "Member 3", "member3@example.com")
        )
    }

    fun loadGroupInfo(groupId: String) {
        // Fetch group info from repository
    }

    fun fetchMembers(groupId: String) {
        val db = FirebaseDatabase.getInstance().reference
        db.child("groups").child(groupId).child("members").get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.children.mapNotNull { memberSnap ->
                    val uid = memberSnap.child("uid").value as? String ?: return@mapNotNull null
                    val name = memberSnap.child("name").value as? String ?: ""
                    val email = memberSnap.child("email").value as? String ?: ""
                    User(uid, name, email)
                }
                _members.value = list
            }
    }

}
