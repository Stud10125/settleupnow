package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.SimplifiedTransaction
import com.example.settleupnow.model.UserBalance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.min

class SummaryViewModel(private val repository: FirebaseRepository = FirebaseRepository()) : ViewModel() {
    private val _balances = MutableStateFlow<List<UserBalance>>(emptyList())
    val balances: StateFlow<List<UserBalance>> = _balances.asStateFlow()

    fun loadSummary(groupId: String) {
        viewModelScope.launch {
            val balanceMap = repository.calculateGroupBalances(groupId)
            val members = repository.getGroupMembers(groupId)
            
            val result = members.map { member ->
                UserBalance(
                    userName = member.name,
                    balance = balanceMap[member.userId] ?: 0.0
                )
            }
            _balances.value = result
        }
    }

    fun getSimplifiedTransactions(): List<SimplifiedTransaction> {
        val debtors = _balances.value.filter { it.balance < -0.01 }.map { it.copy() }.toMutableList()
        val creditors = _balances.value.filter { it.balance > 0.01 }.map { it.copy() }.toMutableList()

        val transactions = mutableListOf<SimplifiedTransaction>()

        var dIdx = 0
        var cIdx = 0

        val dList = debtors.sortedBy { it.balance }.toMutableList()
        val cList = creditors.sortedByDescending { it.balance }.toMutableList()

        while (dIdx < dList.size && cIdx < cList.size) {
            val debtor = dList[dIdx]
            val creditor = cList[cIdx]

            val amount = min(abs(debtor.balance), creditor.balance)
            
            if (amount > 0.01) {
                transactions.add(SimplifiedTransaction(debtor.userName, creditor.userName, amount))
            }

            dList[dIdx] = debtor.copy(balance = debtor.balance + amount)
            cList[cIdx] = creditor.copy(balance = creditor.balance - amount)

            if (abs(dList[dIdx].balance) < 0.01) dIdx++
            if (abs(cList[cIdx].balance) < 0.01) cIdx++
        }

        return transactions
    }
}
