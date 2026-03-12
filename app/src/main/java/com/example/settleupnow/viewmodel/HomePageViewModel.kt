package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.model.Group
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomePageViewModel : ViewModel() {

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    init {
        fetchGroups()
    }

    private fun fetchGroups() {
        val db = FirebaseDatabase.getInstance().reference
        db.child("groups").get()
            .addOnSuccessListener { snapshot ->
                val groupList = mutableListOf<Group>()
                snapshot.children.forEach { groupSnap ->
                    val name = groupSnap.child("name").value as? String ?: ""
                    val desc = groupSnap.child("description").value as? String ?: ""
                    groupList.add(Group(groupSnap.key ?: "", name, desc))
                }
                _groups.value = groupList
            }
            .addOnFailureListener {
                // handle error if needed
            }
    }
}
