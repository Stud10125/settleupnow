package com.example.settleupnow.model

data class Expense(
    val expenseId: String = "",
    val groupId: String = "",
    val title: String = "",
    val description: String = "",
    val amount: Int = 0,
    val paidBy: String = "",
    val paidByName: String = "", // Added to store display name
    val visibility: String = "General",
    val splitType: String = "Equal",
    val splits: Map<String, Int> = emptyMap()
)
