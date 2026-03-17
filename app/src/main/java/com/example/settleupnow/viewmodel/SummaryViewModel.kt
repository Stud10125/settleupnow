package com.example.settleupnow.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.settleupnow.Repository.FirebaseRepository
import com.example.settleupnow.model.Balance
import com.example.settleupnow.model.SimplifiedTransaction
import com.example.settleupnow.model.UserBalance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.PriorityQueue
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
        val balances = _balances.value.filter { Math.abs(it.balance) > 0.01 }

        val debtors = PriorityQueue<Balance> { a, b -> a.balance.compareTo(b.balance) }
        val creditors = PriorityQueue<Balance> { a, b -> b.balance.compareTo(a.balance) }

        balances.forEach { (name, amount) ->
            val balanceObj = Balance(userName = name, balance = amount)
            if (amount < 0) debtors.add(balanceObj) else creditors.add(balanceObj)
        }

        val transactions = mutableListOf<SimplifiedTransaction>()

        while (debtors.isNotEmpty() && creditors.isNotEmpty()) {
            val debtor = debtors.poll()!!
            val creditor = creditors.poll()!!

            val amount = minOf(Math.abs(debtor.balance), creditor.balance)

            if (amount > 0.01) {
                transactions.add(SimplifiedTransaction(debtor.userName, creditor.userName, amount))
            }

            debtor.balance += amount
            creditor.balance -= amount

            if (Math.abs(debtor.balance) > 0.01) debtors.add(debtor)
            if (Math.abs(creditor.balance) > 0.01) creditors.add(creditor)
        }

        return transactions
    }
}
