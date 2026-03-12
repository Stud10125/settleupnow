package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserBalance(
    val userName: String,
    val balance: Double
)

class SummaryViewModel : ViewModel() {
    private val _balances = MutableStateFlow<List<UserBalance>>(emptyList())
    val balances: StateFlow<List<UserBalance>> = _balances.asStateFlow()

    init {
        // Dummy data for now
        _balances.value = listOf(
            UserBalance("Member 1", 50.0),
            UserBalance("Member 2", -20.0),
            UserBalance("Member 3", 0.0)
        )
    }

    fun loadSummary(groupId: String) {
        // Logic to load balances for the group
    }
}
