package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class UserBalance(
    val userName: String,
    val balance: Double
)

class SummaryViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _balances = MutableStateFlow<List<UserBalance>>(emptyList())
    val balances: StateFlow<List<UserBalance>> = _balances.asStateFlow()

    fun loadSummary(groupId: String) {
        repository.calculateGroupBalances(groupId) { balanceMap ->
            repository.getGroupMembers(groupId) { members ->
                val result = members.map { member ->
                    UserBalance(
                        userName = member.name,
                        balance = balanceMap[member.userId] ?: 0.0
                    )
                }
                _balances.value = result
            }
        }
    }
}
