package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EditExpenseViewModel : ViewModel() {
    private val _expense = MutableStateFlow<Expense?>(null)
    val expense: StateFlow<Expense?> = _expense.asStateFlow()

    private val _editedSplits = MutableStateFlow<Map<String, String>>(emptyMap())
    val editedSplits: StateFlow<Map<String, String>> = _editedSplits.asStateFlow()

    init {
        // Dummy initialization for now
        val dummyExpense = Expense(
            expenseId = "1",
            title = "Dinner",
            amount = 500,
            paidBy = "Member 1",
            splits = mapOf(
                "Member 1" to 100,
                "Member 2" to 100,
                "Member 3" to 100,
                "Member 4" to 100,
                "Member 5" to 100
            )
        )
        setExpense(dummyExpense)
    }

    fun setExpense(expense: Expense) {
        _expense.value = expense
        _editedSplits.value = expense.splits.mapValues { it.value.toString() }
    }

    fun onSplitAmountChange(member: String, amount: String) {
        _editedSplits.update { it + (member to amount) }
    }

    fun saveChanges() {
        // Logic to update the expense in the data source
    }
}
