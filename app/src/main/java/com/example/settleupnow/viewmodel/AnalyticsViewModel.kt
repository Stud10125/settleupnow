package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

data class GroupAnalytics(
    val groupId: String,
    val groupName: String,
    val balance: Double
)

class AnalyticsViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _groupBalances = MutableStateFlow<List<GroupAnalytics>>(emptyList())
    val groupBalances: StateFlow<List<GroupAnalytics>> = _groupBalances.asStateFlow()

    private val _totalBalance = MutableStateFlow(0.0)
    val totalBalance: StateFlow<Double> = _totalBalance.asStateFlow()

    fun fetchAnalytics() {
        val currentUserId = repository.getCurrentUserId() ?: return
        
        viewModelScope.launch {
            val groups = repository.getUserGroups()
            
            if (groups.isEmpty()) {
                _groupBalances.value = emptyList()
                _totalBalance.value = 0.0
                return@launch
            }

            val analyticsList = groups.map { group ->
                async {
                    val balance = repository.getUserNetBalanceInGroup(group.id, currentUserId)
                    GroupAnalytics(group.id, group.groupName, balance)
                }
            }.awaitAll()

            _groupBalances.value = analyticsList
            _totalBalance.value = analyticsList.sumOf { it.balance }
        }
    }
}
