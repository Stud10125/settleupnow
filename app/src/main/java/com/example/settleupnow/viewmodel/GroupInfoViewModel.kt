package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class GroupInfoViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _members = MutableStateFlow<List<User>>(emptyList())
    val members: StateFlow<List<User>> = _members.asStateFlow()

    fun fetchMembers(groupId: String) {
        repository.getGroupMembers(groupId) { memberList ->
            _members.value = memberList
        }
    }
}
