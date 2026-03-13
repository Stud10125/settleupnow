package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseInfoViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _expense = MutableStateFlow<Expense?>(null)
    val expense: StateFlow<Expense?> = _expense.asStateFlow()

    private val _participantAmounts = MutableStateFlow<Map<String, Int>>(emptyMap())
    val participantAmounts: StateFlow<Map<String, Int>> = _participantAmounts.asStateFlow()

    fun loadExpense(expenseId: String) {
        viewModelScope.launch {
            val (exp, participantMap) = repository.getExpenseDetails(expenseId)
            _expense.value = exp

            if (exp != null) {
                val members = repository.getGroupMembers(exp.groupId)
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
