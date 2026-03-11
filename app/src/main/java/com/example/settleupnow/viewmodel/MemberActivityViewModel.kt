package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberActivityViewModel : ViewModel(){
    private val _members = MutableStateFlow(
        listOf("Yash", "Aarav", "Neha", "Rohan", "Priya")
    )
    val members: StateFlow<List<String>> = _members.asStateFlow()


    fun setMembers(list: List<String>) {
        _members.value = list
    }

    fun addMember(name: String) {
        val t = name.trim()
        if (t.isNotEmpty()) _members.value = _members.value + t
    }

    fun removeMember(name: String) {
        _members.value = _members.value.filterNot { it.equals(name, ignoreCase = true) }
    }

    fun clear() {
        _members.value = emptyList()
    }

}