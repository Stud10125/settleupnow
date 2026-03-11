package com.example.settleupnow.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Expense(
    val title: String,
    val amount: Int
)

data class GroupDetailUiState(
    val groupName: String = "",
    val expenses: List<Expense> = emptyList()
)

class GroupDetailViewModel(
    private val groupName: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        GroupDetailUiState(
            groupName = groupName,
            expenses = listOf(
                Expense("Breakfast", 1000),
                Expense("Lunch", 2000),
                Expense("Snacks", 500),
                Expense("Dinner", 1600),
                Expense("Travel", 2000)
            )
        )
    )
    val uiState: StateFlow<GroupDetailUiState> = _uiState.asStateFlow()

    fun addExpense(title: String, amount: Int) {
        val t = title.trim()
        if (t.isEmpty() || amount < 0) return
        _uiState.value = _uiState.value.copy(
            expenses = _uiState.value.expenses + Expense(t, amount)
        )
    }

    // Factory to pass groupName from Activity
    class Factory(
        private val groupName: String
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GroupDetailViewModel::class.java)) {
                return GroupDetailViewModel(groupName) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}