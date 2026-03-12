package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.Expense
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupDetailsViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel(){

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName.asStateFlow()

    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    fun fetchGroup(groupId: String) {
        // Fetch group name
        val db = FirebaseDatabase.getInstance().reference
        db.child("groups").child(groupId).child("groupName").get()
            .addOnSuccessListener { snapshot ->
                _groupName.value = snapshot.value as? String ?: ""
            }

        // Fetch expenses
        repository.getGroupExpenses(groupId) { expenseList ->
            _expenses.value = expenseList
        }
    }

    fun addMemberByEmail(groupId: String, email: String, onResult: (Boolean, String) -> Unit) {
        val db = FirebaseDatabase.getInstance().reference
        db.child("users").orderByChild("email").equalTo(email).get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val userSnap = snapshot.children.first()
                val userId = userSnap.key ?: return@addOnSuccessListener
                
                repository.addMemberToGroup(groupId, userId) { success, msg ->
                    onResult(success, msg)
                }
            } else {
                onResult(false, "User not found")
            }
        }.addOnFailureListener {
            onResult(false, it.message ?: "Error looking up user")
        }
    }
}
