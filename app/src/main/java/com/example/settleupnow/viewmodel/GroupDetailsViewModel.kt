package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.Expense
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class GroupDetailsViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel(){

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    fun fetchGroup(groupId: String) {
        viewModelScope.launch {
            try {
                // Fetch group name
                val db = FirebaseDatabase.getInstance().reference
                val snapshot = db.child("groups").child(groupId).child("groupName").get().await()
                _groupName.value = snapshot.value as? String ?: ""

                // Fetch expenses
                val expenseList = repository.getGroupExpenses(groupId)
                _expenses.value = expenseList
            } catch (e: Exception) {
                // Handle errors if necessary
            }
        }
    }

    fun addMemberByEmail(groupId: String, email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            try {
                val db = FirebaseDatabase.getInstance().reference
                val snapshot = db.child("users").orderByChild("email").equalTo(email).get().await()
                
                if (snapshot.exists()) {
                    val userSnap = snapshot.children.first()
                    val userId = userSnap.key ?: throw Exception("User key missing")
                    
                    val (success, msg) = repository.addMemberToGroup(groupId, userId)
                    onResult(success, msg)
                } else {
                    onResult(false, "User not found")
                }
            } catch (e: Exception) {
                onResult(false, e.message ?: "Error looking up user")
            }
        }
    }
}
