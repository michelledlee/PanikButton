package com.example.panikbutton.data

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

abstract class ReadAndWriteSnippets {

    private val TAG = "ReadAndWriteSnippets"

    private lateinit var database: DatabaseReference

    /** Initialize a database reference **/
    fun initializeDbRef() {
        database = Firebase.database.reference
    }

    /** Add a new user to the database **/
    fun writeNewUser(userName: String, userPhone: Long, userEmail: String) {
        val user = User(userName, userPhone, userEmail)

        // Access userId from shared preferences
        val userId = ""
        database.child(userId).child(userName).setValue(user)
    }

    /** Add a new user to the database with success/failure listeners **/
    fun writeNewUserWithTaskListeners(userName: String, userPhone: Long, userEmail: String) {
        val user = User(userName, userPhone, userEmail)

        // Access userId from shared preferences
        val userId = ""

        database.child(userId).child(userName).setValue(user)
            .addOnSuccessListener {
                // Write was successful!
                // ...
            }
            .addOnFailureListener {
                // Write failed
                // ...
            }
    }

    /** Add a new contact to the database **/
    fun writeNewContact(id: Long, contactName: String, contactPhone: Long, contactEmail: String) {
        val user = Contact(id, contactName, contactPhone, contactEmail)

        database.child("contacts").child(contactName).setValue(user)
    }
}