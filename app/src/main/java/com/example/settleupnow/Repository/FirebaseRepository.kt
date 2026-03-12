package com.example.settleupnow.Repository

import com.example.settleupnow.model.User
import com.example.settleupnow.model.Group
import com.example.settleupnow.model.Expense
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    fun getCurrentUserId(): String? = auth.currentUser?.uid

    fun register(email: String, password: String, name: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val userMap = mapOf(
                        "userId" to uid,
                        "name" to name,
                        "email" to email
                    )
                    db.child("users").child(uid).setValue(userMap)
                    onResult(true, "Registration successful")
                } else {
                    onResult(false, task.exception?.message ?: "Registration failed")
                }
            }
    }

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful)
                    onResult(true, "Login success")
                else
                    onResult(false, "Login failed")
            }
    }

    fun getUserGroups(onGroupsLoaded: (List<Group>) -> Unit) {
        val uid = getCurrentUserId() ?: return onGroupsLoaded(emptyList())

        db.child("userGroups").child(uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val groupIds = snapshot.children.mapNotNull { it.key }
                if (groupIds.isEmpty()) {
                    onGroupsLoaded(emptyList())
                    return
                }

                val loadedGroups = mutableListOf<Group>()
                var count = 0
                for (groupId in groupIds) {
                    db.child("groups").child(groupId).get().addOnSuccessListener { groupSnap ->
                        val group = Group(
                            id = groupSnap.key ?: "",
                            groupName = groupSnap.child("groupName").value as? String 
                                ?: groupSnap.child("name").value as? String ?: "Unnamed Group",
                            description = groupSnap.child("description").value as? String ?: ""
                        )
                        loadedGroups.add(group)
                        count++
                        if (count == groupIds.size) {
                            onGroupsLoaded(loadedGroups)
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                onGroupsLoaded(emptyList())
            }
        })
    }

    fun getGroupMembers(groupId: String, onResult: (List<User>) -> Unit) {
        db.child("groupMembers").child(groupId).get().addOnSuccessListener { snapshot ->
            val memberIds = snapshot.children.mapNotNull { it.key }
            if (memberIds.isEmpty()) {
                onResult(emptyList())
                return@addOnSuccessListener
            }

            val users = mutableListOf<User>()
            var count = 0
            memberIds.forEach { userId ->
                db.child("users").child(userId).get().addOnSuccessListener { userSnap ->
                    val email = userSnap.child("email").value as? String ?: ""
                    val name = userSnap.child("name").value as? String 
                        ?: userSnap.child("displayName").value as? String
                        ?: email.substringBefore("@").ifEmpty { "Member" }
                    
                    val user = User(
                        userId = userId,
                        name = name,
                        email = email
                    )
                    users.add(user)
                    count++
                    if (count == memberIds.size) onResult(users)
                }.addOnFailureListener {
                    count++
                    if (count == memberIds.size) onResult(users)
                }
            }
        }.addOnFailureListener {
            onResult(emptyList())
        }
    }

    fun getGroupExpenses(groupId: String, onResult: (List<Expense>) -> Unit) {
        db.child("groupExpenses").child(groupId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val expenseIds = snapshot.children.mapNotNull { it.key }
                if (expenseIds.isEmpty()) {
                    onResult(emptyList())
                    return
                }

                val expenses = mutableListOf<Expense>()
                var count = 0
                expenseIds.forEach { id ->
                    db.child("expenses").child(id).get().addOnSuccessListener { snap ->
                        val expense = Expense(
                            expenseId = snap.key ?: "",
                            groupId = snap.child("groupId").value as? String ?: "",
                            title = snap.child("title").value as? String ?: "",
                            amount = (snap.child("amount").value as? Long)?.toInt() ?: 0,
                            paidBy = snap.child("paidBy").value as? String ?: "",
                            paidByName = snap.child("paidByName").value as? String ?: "Unknown"
                        )
                        expenses.add(expense)
                        count++
                        if (count == expenseIds.size) {
                            onResult(expenses.sortedByDescending { it.expenseId })
                        }
                    }.addOnFailureListener {
                        count++
                        if (count == expenseIds.size) onResult(expenses)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun getExpenseDetails(expenseId: String, onResult: (Expense?, Map<String, Int>) -> Unit) {
        db.child("expenses").child(expenseId).get().addOnSuccessListener { snap ->
            if (!snap.exists()) return@addOnSuccessListener onResult(null, emptyMap())
            
            val expense = Expense(
                expenseId = snap.key ?: "",
                groupId = snap.child("groupId").value as? String ?: "",
                title = snap.child("title").value as? String ?: "",
                amount = (snap.child("amount").value as? Long)?.toInt() ?: 0,
                paidBy = snap.child("paidBy").value as? String ?: "",
                paidByName = snap.child("paidByName").value as? String ?: "Unknown"
            )

            db.child("expenseParticipants").child(expenseId).get().addOnSuccessListener { partSnap ->
                val participants = mutableMapOf<String, Int>()
                partSnap.children.forEach { participant ->
                    val userId = participant.key ?: return@forEach
                    val amount = (participant.value as? Long)?.toInt() ?: 0
                    participants[userId] = amount
                }
                onResult(expense, participants)
            }
        }
    }

    fun calculateGroupBalances(groupId: String, onResult: (Map<String, Double>) -> Unit) {
        val balances = mutableMapOf<String, Double>()

        db.child("groupExpenses").child(groupId).get().addOnSuccessListener { snapshot ->
            val expenseIds = snapshot.children.mapNotNull { it.key }
            if (expenseIds.isEmpty()) return@addOnSuccessListener onResult(emptyMap())

            var processedCount = 0
            expenseIds.forEach { expId ->
                db.child("expenses").child(expId).get().addOnSuccessListener { expSnap ->
                    val payerId = expSnap.child("paidBy").value as? String ?: ""
                    val totalAmount = (expSnap.child("amount").value as? Long)?.toDouble() ?: 0.0

                    balances[payerId] = (balances[payerId] ?: 0.0) + totalAmount

                    db.child("expenseParticipants").child(expId).get().addOnSuccessListener { partSnap ->
                        partSnap.children.forEach { participant ->
                            val userId = participant.key ?: return@forEach
                            val owedAmount = (participant.value as? Long)?.toDouble() ?: 0.0
                            balances[userId] = (balances[userId] ?: 0.0) - owedAmount
                        }

                        processedCount++
                        if (processedCount == expenseIds.size) {
                            onResult(balances)
                        }
                    }
                }
            }
        }
    }

    fun getUserNetBalanceInGroup(groupId: String, userId: String, onResult: (Double) -> Unit) {
        var balance = 0.0
        db.child("groupExpenses").child(groupId).get().addOnSuccessListener { snapshot ->
            val expenseIds = snapshot.children.mapNotNull { it.key }
            if (expenseIds.isEmpty()) return@addOnSuccessListener onResult(0.0)

            var count = 0
            expenseIds.forEach { id ->
                db.child("expenses").child(id).get().addOnSuccessListener { expSnap ->
                    val payer = expSnap.child("paidBy").value as? String ?: ""
                    val amount = (expSnap.child("amount").value as? Long)?.toDouble() ?: 0.0
                    
                    if (payer == userId) balance += amount

                    db.child("expenseParticipants").child(id).child(userId).get().addOnSuccessListener { partSnap ->
                        val userOwes = (partSnap.value as? Long)?.toDouble() ?: 0.0
                        balance -= userOwes
                        
                        count++
                        if (count == expenseIds.size) onResult(balance)
                    }
                }
            }
        }
    }

    fun createGroup(name: String, description: String, members: List<User>, onResult: (Boolean, String) -> Unit) {
        val currentUserId = getCurrentUserId() ?: return onResult(false, "User not logged in")
        val groupId = db.child("groups").push().key ?: return onResult(false, "Failed to generate ID")

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

        db.updateChildren(updates)
            .addOnSuccessListener { onResult(true, "Group created successfully") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed to create group") }
    }

    fun addMemberToGroup(groupId: String, userId: String, onResult: (Boolean, String) -> Unit) {
        val updates = mapOf(
            "/groupMembers/$groupId/$userId" to true,
            "/userGroups/$userId/$groupId" to true
        )
        db.updateChildren(updates)
            .addOnSuccessListener { onResult(true, "Member added") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed to add member") }
    }

    fun addExpense(
        groupId: String,
        title: String,
        amount: Int,
        paidBy: String,
        paidByName: String,
        participants: Map<String, Int>,
        onResult: (Boolean, String) -> Unit
    ) {
        val expenseId = db.child("expenses").push().key ?: return onResult(false, "Failed to generate ID")
        
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

        db.updateChildren(updates)
            .addOnSuccessListener { onResult(true, "Expense added successfully") }
            .addOnFailureListener { onResult(false, it.message ?: "Failed to add expense") }
    }

    fun updateExpense(
        expenseId: String,
        title: String,
        amount: Int,
        participants: Map<String, Int>,
        onResult: (Boolean, String) -> Unit
    ) {
        val updates = mutableMapOf<String, Any?>()
        updates["/expenses/$expenseId/title"] = title
        updates["/expenses/$expenseId/amount"] = amount
        
        // Remove old participants and set new ones
        db.child("expenseParticipants").child(expenseId).setValue(participants)
            .addOnSuccessListener {
                db.updateChildren(updates)
                    .addOnSuccessListener { onResult(true, "Updated successfully") }
                    .addOnFailureListener { onResult(false, it.message ?: "Failed metadata") }
            }
            .addOnFailureListener { onResult(false, it.message ?: "Failed splits") }
    }
}
