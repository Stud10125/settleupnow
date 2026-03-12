package com.example.settleupnow.Repository

import com.example.settleupnow.model.User
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.auth.FirebaseAuth

class FirebaseRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().reference

    fun register(email: String, password: String, name: String, onResult: (Boolean, String) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = User(uid, name, email)
                    db.child("users").child(uid).setValue(user)
                    onResult(true, "Registration successful")
                } else {
                    onResult(false, task.exception?.message ?: "Registration failed")
                }
            }
    }

    fun login(email:String,password:String,
              onResult:(Boolean,String)->Unit){

        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {

                if(it.isSuccessful)
                    onResult(true,"Login success")
                else
                    onResult(false,"Login failed")
            }
    }

    fun saveUserToDatabase(uid: String, email: String) {
        val userMap = mapOf(
            "uid" to uid,
            "email" to email
        )
        db.child("users").child(uid).setValue(userMap)
    }

}