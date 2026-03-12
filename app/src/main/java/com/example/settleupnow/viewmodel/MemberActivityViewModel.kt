package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MemberActivityViewModel : ViewModel() {
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses.asStateFlow()

    private val _groupName = MutableStateFlow("")
    val groupName: StateFlow<String> = _groupName.asStateFlow()

    init {
        // Dummy data for now
        _groupName.value = "Roommates"
        _expenses.value = listOf(
            Expense(expenseId = "1", title = "Rent", amount = 1500, paidBy = "Member 1"),
            Expense(expenseId = "2", title = "Groceries", amount = 200, paidBy = "Member 2")
        )
    }

    fun loadGroupData(groupId: String) {
        // Fetch group name and expenses from repository
    }
}
