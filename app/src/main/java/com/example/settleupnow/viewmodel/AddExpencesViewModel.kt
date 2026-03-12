package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AddExpencesViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private var groupId: String = ""

    private val _expenceTitle = MutableStateFlow("")
    val expenceTitle: StateFlow<String> = _expenceTitle

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _splitType = MutableStateFlow("Equal")
    val splitType: StateFlow<String> = _splitType

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _paidBy = MutableStateFlow("")
    val paidBy: StateFlow<String> = _paidBy
    
    // Store the actual user ID of the payer
    private var paidByUserId: String = ""

    private val _members = MutableStateFlow<List<User>>(emptyList())
    val members: StateFlow<List<User>> = _members

    private val _checkedList = MutableStateFlow<List<Boolean>>(emptyList())
    val checkedList: StateFlow<List<Boolean>> = _checkedList

    private val _expencesList = MutableStateFlow<List<String>>(emptyList())
    val expencesList: StateFlow<List<String>> = _expencesList

    private val _total = MutableStateFlow(0)
    val total: StateFlow<Int> = _total

    fun setGroupId(id: String) {
        this.groupId = id
        fetchMembers()
    }

    private fun fetchMembers() {
        repository.getGroupMembers(groupId) { memberList ->
            _members.value = memberList
            _checkedList.value = List(memberList.size) { true }
            _expencesList.value = List(memberList.size) { "" }
            if (memberList.isNotEmpty()) {
                val currentUser = repository.getCurrentUserId()
                val defaultPayer = memberList.find { it.userId == currentUser } ?: memberList.first()
                _paidBy.value = defaultPayer.name
                paidByUserId = defaultPayer.userId
            }
        }
    }

    fun expenceTitle(title: String) { _expenceTitle.value = title }
    fun description(description: String) { _description.value = description }
    fun splitType(type: String) { _splitType.value = type }
    fun _amount(amt: String) { _amount.value = amt }
    
    fun paidBy(userName: String) { 
        _paidBy.value = userName
        paidByUserId = _members.value.find { it.name == userName }?.userId ?: ""
    }

    fun checkedList(index: Int, checked: Boolean) {
        _checkedList.update { currentList ->
            currentList.toMutableList().apply { if (index < size) this[index] = checked }
        }
    }

    fun expencesList(index: Int, amountStr: String) {
        _expencesList.update { currentList ->
            currentList.toMutableList().apply { if (index < size) this[index] = amountStr }
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

    fun saveExpense(onResult: (Boolean, String) -> Unit) {
        val title = _expenceTitle.value
        val totalAmount = if (_splitType.value == "Equal") _amount.value.toIntOrNull() ?: 0 else _total.value
        
        val participants = mutableMapOf<String, Int>()
        
        if (_splitType.value == "Equal") {
            val selectedMembers = _members.value.filterIndexed { index, _ -> _checkedList.value.getOrElse(index) { false } }
            if (selectedMembers.isEmpty()) return onResult(false, "Select at least one member")
            
            val splitAmount = totalAmount / selectedMembers.size
            selectedMembers.forEach { participants[it.userId] = splitAmount }
        } else {
            _members.value.forEachIndexed { index, user ->
                val memberAmount = _expencesList.value.getOrElse(index) { "" }
                    .split("+").mapNotNull { it.trim().toIntOrNull() }.sum()
                if (memberAmount > 0) participants[user.userId] = memberAmount
            }
        }

        if (participants.isEmpty()) return onResult(false, "No participants in expense")

        repository.addExpense(
            groupId = groupId,
            title = title,
            amount = totalAmount,
            paidBy = paidByUserId,
            paidByName = _paidBy.value, // Added passing name
            participants = participants,
            onResult = onResult
        )
    }
}
