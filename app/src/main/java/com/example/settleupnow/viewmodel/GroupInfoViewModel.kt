package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupInfoViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _members = MutableStateFlow<List<User>>(emptyList())
    val members: StateFlow<List<User>> = _members.asStateFlow()

    fun fetchMembers(groupId: String) {
        viewModelScope.launch {
            val memberList = repository.getGroupMembers(groupId)
            _members.value = memberList
        }
    }
}
