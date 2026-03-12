package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

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
        
        repository.getUserGroups { groups ->
            val analyticsList = mutableListOf<GroupAnalytics>()
            var processedCount = 0
            var runningTotal = 0.0

            if (groups.isEmpty()) {
                _groupBalances.value = emptyList()
                _totalBalance.value = 0.0
                return@getUserGroups
            }

            groups.forEach { group ->
                repository.getUserNetBalanceInGroup(group.id, currentUserId) { balance ->
                    analyticsList.add(GroupAnalytics(group.id, group.groupName, balance))
                    runningTotal += balance
                    processedCount++
                    
                    if (processedCount == groups.size) {
                        _groupBalances.value = analyticsList
                        _totalBalance.value = runningTotal
                    }
                }
            }
        }
    }
}
