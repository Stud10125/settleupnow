package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AddGroupViewModel : ViewModel() {

    private val _groups = MutableStateFlow<List<String>>(emptyList())
    val groups: StateFlow<List<String>> = _groups.asStateFlow()

    fun addGroup(name: String) {
        val trimmed = name.trim()
        if (trimmed.isNotEmpty()) {
            _groups.value = _groups.value + trimmed
        }
    }


}