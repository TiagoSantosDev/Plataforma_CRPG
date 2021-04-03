package com.plataforma.crpg.ui.reminders

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReminderRepository {

    private lateinit var database: DatabaseReference

    fun initializeDbRef() {
        // [START initialize_database_ref]
        database = Firebase.database.reference
        // [END initialize_database_ref]
    }

/*
    fun writeNewUser(userId: String, name: String, email: String) {
        val reminder = User(name, email)

        database.child("users").child(userId).setValue(user)
    }
*/



}