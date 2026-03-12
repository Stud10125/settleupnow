package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseInfoViewModel : ViewModel() {
    private val _expense = MutableStateFlow<Expense?>(null)
    val expense: StateFlow<Expense?> = _expense.asStateFlow()

    init {
        // Dummy data
        _expense.value = Expense(
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
    }

    fun loadExpense(expenseId: String) {
        // Fetch expense details
    }
}
