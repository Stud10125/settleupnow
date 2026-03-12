package com.example.settleupnow.viewmodel

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

    fun createGroup() {
        // Logic to save group
    }
}