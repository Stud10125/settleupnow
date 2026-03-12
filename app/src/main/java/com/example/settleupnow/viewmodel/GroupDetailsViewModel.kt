package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupDetailsViewModel : ViewModel(){

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName.asStateFlow()

    fun fetchGroup(groupId: String) {
        val db = FirebaseDatabase.getInstance().reference
        db.child("groups").child(groupId).child("name").get()
            .addOnSuccessListener { snapshot ->
                _groupName.value = snapshot.value as? String ?: ""
            }
    }

}