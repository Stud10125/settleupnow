package com.example.settleupnow.viewmodel


import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AddExpencesViewModel : ViewModel() {
    private val _expenceTitle = MutableStateFlow("")
    val expenceTitle: StateFlow<String> = _expenceTitle

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _splitType = MutableStateFlow("Equal")
    val splitType: StateFlow<String> = _splitType

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _paidBy = MutableStateFlow("Member 1")
    val paidBy: StateFlow<String> = _paidBy

    private val _checkedList = MutableStateFlow(List(10) { index -> index == 0 })
    val checkedList: StateFlow<List<Boolean>> = _checkedList

    private val _expencesList = MutableStateFlow(List(10) { "" })
    val expencesList: StateFlow<List<String>> = _expencesList

    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> = _total

    fun expenceTitle(title: String) { _expenceTitle.value = title }
    fun description(description: String) { _description.value = description }
    fun splitType(splitType: String) { _splitType.value = splitType }
    fun _amount(amount: String) { _amount.value = amount }
    fun paidBy(member: String) { _paidBy.value = member }

    fun checkedList(index: Int, checked: Boolean) {
        _checkedList.update { currentList ->
            currentList.toMutableList().apply { this[index] = checked }
        }
    }

    fun expencesList(index: Int, amount: String) {
        _expencesList.update { currentList ->
            currentList.toMutableList().apply { this[index] = amount }
        }
        calculateTotal()
    }

    fun calculateTotal() {
        var sum = 0
        for (amountStr in _expencesList.value) {
            val totalForMember = amountStr.split("+")
                .map { it.trim() }
                .filter { it.isNotEmpty() }
                .mapNotNull { it.toIntOrNull() }
                .sum()
            sum += totalForMember
        }
        _total.value = sum
    }
}
