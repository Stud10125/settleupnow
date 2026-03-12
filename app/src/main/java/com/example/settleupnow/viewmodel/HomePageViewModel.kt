package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.model.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomePageViewModel : ViewModel() {
    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    init {
        // Dummy data for now
        _groups.value = listOf(
            Group(groupId = "1", groupName = "Roommates", description = "Monthly bills"),
            Group(groupId = "2", groupName = "Goa Trip", description = "Weekend fun")
        )
    }

    fun loadGroups() {
        // Logic to fetch groups from a repository
    }
}
