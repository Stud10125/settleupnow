package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditExpenseViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _expense = MutableStateFlow<Expense?>(null)
    val expense: StateFlow<Expense?> = _expense.asStateFlow()

    // Map of Name -> String Amount for UI
    private val _editedSplits = MutableStateFlow<Map<String, String>>(emptyMap())
    val editedSplits: StateFlow<Map<String, String>> = _editedSplits.asStateFlow()

    // Map of Name -> UserId for reverse mapping
    private var memberNameToId = mutableMapOf<String, String>()

    fun loadExpense(expenseId: String) {
        repository.getExpenseDetails(expenseId) { exp, participants ->
            _expense.value = exp
            if (exp != null) {
                repository.getGroupMembers(exp.groupId) { members ->
                    val uiSplits = mutableMapOf<String, String>()
                    participants.forEach { (uid, amount) ->
                        val user = members.find { it.userId == uid }
                        val name = user?.name ?: "Unknown"
                        uiSplits[name] = amount.toString()
                        memberNameToId[name] = uid
                    }
                    _editedSplits.value = uiSplits
                }
            }
        }
    }

    fun onSplitAmountChange(member: String, amount: String) {
        _editedSplits.update { it + (member to amount) }
    }

    fun saveChanges(onResult: (Boolean) -> Unit) {
        val exp = _expense.value ?: return
        val newSplits = mutableMapOf<String, Int>()
        var newTotal = 0
        
        _editedSplits.value.forEach { (name, amountStr) ->
            val userId = memberNameToId[name] ?: return@forEach
            val amount = amountStr.toIntOrNull() ?: 0
            newSplits[userId] = amount
            newTotal += amount
        }

        repository.updateExpense(
            expenseId = exp.expenseId,
            title = exp.title,
            amount = newTotal,
            participants = newSplits,
            onResult = { success, _ -> onResult(success) }
        )
    }
}
