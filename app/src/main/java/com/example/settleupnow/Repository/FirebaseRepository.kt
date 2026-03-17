package com.example.settleupnow.Repository

import android.util.Log
import com.example.settleupnow.model.User
import com.example.settleupnow.model.Group
import com.example.settleupnow.model.Expense
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll

class FirebaseRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference
    fun getCurrentUserId(): String? = auth.currentUser?.uid
    suspend fun register(email: String, password: String, name: String): Pair<Boolean, String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Pair(false, "User creation failed")
            val userMap = mapOf(
                "userId" to uid,
                "name" to name,
                "email" to email
            )
            db.child("users").child(uid).setValue(userMap).await()
            Pair(true, "Registration successful")
        } catch (e: Exception) {
            Pair(false, e.message ?: "Registration failed")
        }
    }

    suspend fun login(email: String, password: String): Pair<Boolean, String> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            Pair(true, "Login success")
        } catch (e: FirebaseAuthInvalidUserException) {
            Pair(false, "No account found with this email.")
        } catch (e: FirebaseAuthInvalidCredentialsException) {
            Pair(false, "Incorrect password. Please try again.")
        } catch (e: Exception) {
            Pair(false, e.message ?: "Login failed. Please check your connection.")
        }
    }

    suspend fun getUserGroups(): List<Group> = withContext(Dispatchers.IO) {
        val uid = getCurrentUserId() ?: return@withContext emptyList()
        try {
            val snapshot = db.child("userGroups").child(uid).get().await()
            val groupIds = snapshot.children.mapNotNull { it.key }
            if (groupIds.isEmpty()) return@withContext emptyList()

            groupIds.map { groupId ->
                async {
                    val groupSnap = db.child("groups").child(groupId).get().await()
                    Group(
                        id = groupSnap.key ?: "",
                        groupName = groupSnap.child("groupName").value as? String 
                            ?: groupSnap.child("name").value as? String ?: "Unnamed Group",
                        description = groupSnap.child("description").value as? String ?: ""
                    )
                }
            }.awaitAll()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getGroupMembers(groupId: String): List<User> = withContext(Dispatchers.IO) {
        try {
            val snapshot = db.child("groupMembers").child(groupId).get().await()
            val memberIds = snapshot.children.mapNotNull { it.key }
            if (memberIds.isEmpty()) return@withContext emptyList()

            memberIds.map { userId ->
                async {
                    val userSnap = db.child("users").child(userId).get().await()
                    val email = userSnap.child("email").value as? String ?: ""
                    val name = userSnap.child("name").value as? String 
                        ?: userSnap.child("displayName").value as? String
                        ?: email.substringBefore("@").ifEmpty { "Member" }
                    
                    User(userId = userId, name = name, email = email)
                }
            }.awaitAll()
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getGroupExpenses(groupId: String): List<Expense> = withContext(Dispatchers.IO) {
        try {
            val snapshot = db.child("groupExpenses").child(groupId).get().await()
            val expenseIds = snapshot.children.mapNotNull { it.key }
            if (expenseIds.isEmpty()) return@withContext emptyList()

            expenseIds.map { id ->
                async {
                    val snap = db.child("expenses").child(id).get().await()
                    Expense(
                        expenseId = snap.key ?: "",
                        groupId = snap.child("groupId").value as? String ?: "",
                        title = snap.child("title").value as? String ?: "",
                        amount = (snap.child("amount").value as? Long)?.toInt() ?: 0,
                        paidBy = snap.child("paidBy").value as? String ?: "",
                        paidByName = snap.child("paidByName").value as? String ?: "Unknown"
                    )
                }
            }.awaitAll().sortedByDescending { it.expenseId }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getExpenseDetails(expenseId: String): Pair<Expense?, Map<String, Int>> = withContext(Dispatchers.IO) {
        try {
            val snap = db.child("expenses").child(expenseId).get().await()
            if (!snap.exists()) return@withContext Pair(null, emptyMap())
            
            val expense = Expense(
                expenseId = snap.key ?: "",
                groupId = snap.child("groupId").value as? String ?: "",
                title = snap.child("title").value as? String ?: "",
                amount = (snap.child("amount").value as? Long)?.toInt() ?: 0,
                paidBy = snap.child("paidBy").value as? String ?: "",
                paidByName = snap.child("paidByName").value as? String ?: "Unknown"
            )

            val partSnap = db.child("expenseParticipants").child(expenseId).get().await()
            val participants = partSnap.children.associate { 
                (it.key ?: "") to ((it.value as? Long)?.toInt() ?: 0)
            }
            Pair(expense, participants)
        } catch (e: Exception) {
            Pair(null, emptyMap())
        }
    }

    suspend fun calculateGroupBalances(groupId: String): Map<String, Double> = withContext(Dispatchers.IO) {
        val balances = mutableMapOf<String, Double>()
        try {
            val snapshot = db.child("groupExpenses").child(groupId).get().await()
            val expenseIds = snapshot.children.mapNotNull { it.key }
            if (expenseIds.isEmpty()) return@withContext emptyMap()

            expenseIds.map { expId ->
                async {
                    val expSnap = db.child("expenses").child(expId).get().await()
                    val payerId = expSnap.child("paidBy").value as? String ?: ""
                    val totalAmount = (expSnap.child("amount").value as? Long)?.toDouble() ?: 0.0

                    synchronized(balances) {
                        balances[payerId] = (balances[payerId] ?: 0.0) + totalAmount
                    }

                    val partSnap = db.child("expenseParticipants").child(expId).get().await()
                    partSnap.children.forEach { participant ->
                        val userId = participant.key ?: return@forEach
                        val owedAmount = (participant.value as? Long)?.toDouble() ?: 0.0
                        synchronized(balances) {
                            balances[userId] = (balances[userId] ?: 0.0) - owedAmount
                        }
                    }
                }
            }.awaitAll()
            balances
        } catch (e: Exception) {
            emptyMap()
        }
    }

    suspend fun getUserNetBalanceInGroup(groupId: String, userId: String): Double = withContext(Dispatchers.IO) {
        try {
            val snapshot = db.child("groupExpenses").child(groupId).get().await()
            val expenseIds = snapshot.children.mapNotNull { it.key }
            if (expenseIds.isEmpty()) return@withContext 0.0

            val results = expenseIds.map { id ->
                async {
                    val expSnap = db.child("expenses").child(id).get().await()
                    val payer = expSnap.child("paidBy").value as? String ?: ""
                    val amount = (expSnap.child("amount").value as? Long)?.toDouble() ?: 0.0
                    
                    var b = 0.0
                    if (payer == userId) b += amount

                    val partSnap = db.child("expenseParticipants").child(id).child(userId).get().await()
                    val userOwes = (partSnap.value as? Long)?.toDouble() ?: 0.0
                    b - userOwes
                }
            }.awaitAll()
            results.sum()
        } catch (e: Exception) {
            0.0
        }
    }

    suspend fun createGroup(name: String, description: String, members: List<User>): Pair<Boolean, String> {
        val currentUserId = getCurrentUserId() ?: return Pair(false, "User not logged in")
        val groupId = db.child("groups").push().key ?: return Pair(false, "Failed to generate ID")

        val groupData = mapOf(
            "id" to groupId,
            "groupName" to name,
            "description" to description,
            "createdBy" to currentUserId,
            "timestamp" to System.currentTimeMillis()
        )

        val updates = mutableMapOf<String, Any?>()
        updates["/groups/$groupId"] = groupData
        
        val allMembers = members.toMutableList()
        if (allMembers.none { it.userId == currentUserId }) {
             updates["/groupMembers/$groupId/$currentUserId"] = true
             updates["/userGroups/$currentUserId/$groupId"] = true
        }

        allMembers.forEach { user ->
            updates["/groupMembers/$groupId/${user.userId}"] = true
            updates["/userGroups/${user.userId}/$groupId"] = true
        }

        return try {
            db.updateChildren(updates).await()
            Pair(true, "Group created successfully")
        } catch (e: Exception) {
            Pair(false, e.message ?: "Failed to create group")
        }
    }

    suspend fun addMemberToGroup(groupId: String, userId: String): Pair<Boolean, String> {
        val updates = mapOf(
            "/groupMembers/$groupId/$userId" to true,
            "/userGroups/$userId/$groupId" to true
        )
        return try {
            db.updateChildren(updates).await()
            Pair(true, "Member added")
        } catch (e: Exception) {
            Pair(false, e.message ?: "Failed to add member")
        }
    }

    suspend fun addExpense(
        groupId: String,
        title: String,
        amount: Int,
        paidBy: String,
        paidByName: String,
        participants: Map<String, Int>
    ): Pair<Boolean, String> {
        val expenseId = db.child("expenses").push().key ?: return Pair(false, "Failed to generate ID")
        
        val expenseData = mapOf(
            "expenseId" to expenseId,
            "groupId" to groupId,
            "title" to title,
            "amount" to amount,
            "paidBy" to paidBy,
            "paidByName" to paidByName,
            "timestamp" to System.currentTimeMillis()
        )

        val updates = mutableMapOf<String, Any?>()
        updates["/expenses/$expenseId"] = expenseData
        updates["/groupExpenses/$groupId/$expenseId"] = true
        
        participants.forEach { (userId, owedAmount) ->
            updates["/expenseParticipants/$expenseId/$userId"] = owedAmount
            updates["/userInvolvedExpenses/$userId/$expenseId"] = true
        }

        return try {
            db.updateChildren(updates).await()
            Pair(true, "Expense added successfully")
        } catch (e: Exception) {
            Pair(false, e.message ?: "Failed to add expense")
        }
    }

    suspend fun updateExpense(
        expenseId: String,
        title: String,
        amount: Int,
        participants: Map<String, Int>
    ): Pair<Boolean, String> {
        return try {
            db.child("expenseParticipants").child(expenseId).setValue(participants).await()
            val updates = mapOf(
//                "/expenses/$expenseId/title" to title,
                "/expenses/$expenseId/amount" to amount
            )
            db.updateChildren(updates).await()
            Pair(true, "Updated successfully")
        } catch (e: Exception) {
            Pair(false, e.message ?: "Failed update")
        }
    }
}
