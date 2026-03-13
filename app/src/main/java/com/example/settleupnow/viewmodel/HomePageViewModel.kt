package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.Group
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomePageViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {

    private val _groups = MutableStateFlow<List<Group>>(emptyList())
    val groups: StateFlow<List<Group>> = _groups.asStateFlow()

    init {
        fetchUserGroups()
    }

    private fun fetchUserGroups() {
        viewModelScope.launch {
            val groupList = repository.getUserGroups()
            _groups.value = groupList
        }
    }
}
