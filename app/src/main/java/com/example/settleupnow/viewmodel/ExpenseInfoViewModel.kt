package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseInfoViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _expense = MutableStateFlow<Expense?>(null)
    val expense: StateFlow<Expense?> = _expense.asStateFlow()

    private val _participantAmounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val participantAmounts: StateFlow<Map<String, Int>> = _participantAmounts.asStateFlow()

    fun loadExpense(expenseId: String) {
        repository.getExpenseDetails(expenseId) { exp, participantMap ->
            _expense.value = exp
            
            // Map User IDs to Names for display
            if (exp != null) {
                repository.getGroupMembers(exp.groupId) { members ->
                    val nameMap = mutableMapOf<String, Int>()
                    participantMap.forEach { (uid, amount) ->
                        val userName = members.find { it.userId == uid }?.name ?: "Unknown"
                        nameMap[userName] = amount
                    }
                    _participantAmounts.value = nameMap
                }
            }
        }
    }
}
