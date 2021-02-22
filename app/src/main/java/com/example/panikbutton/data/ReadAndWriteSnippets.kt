package com.example.panikbutton.data

import android.util.Log
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

interface ReadAndWriteSnippets {
//abstract class ReadAndWriteSnippets {

    var database: DatabaseReference

    /** Initialize a database reference **/
    fun initializeDbRef() {
        database = Firebase.database.reference
    }

    /** Add a new user to the database **/
    fun writeNewUser(userId: Long, userName: String, userPhone: Long, userEmail: String) {
        val user = User(userId, userName, userPhone, userEmail)
        database.child("users").child(userId.toString()).setValue(user)

    }

    /** Edit existing contact**/
    fun editUser(userId: Long, userName: String, userPhone: Long, userEmail: String) {
        val user = User(userId, userName, userPhone, userEmail)
        database.child("users").child(userId.toString()).setValue(user)

    }

    /** Add a new user to the database with success/failure listeners **/
    fun writeNewUserWithTaskListeners(userName: String, userPhone: Long, userEmail: String) {
        val userId = Random.nextLong()
        val user = User(userId, userName, userPhone, userEmail)

        // Access userId from shared preferences

        database.child(userId.toString()).child(userName).setValue(user)
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
    fun writeNewContact(userId: Long, contactId: Int, contactName: String, contactPhone: Long, contactEmail: String) {
        val contact = Contact(contactId, contactName, contactPhone, contactEmail)

        database.child("users").child(userId.toString()).child("contacts").child("contactId").child(contactName).setValue(contact)
    }

    /** Get contact based on ID **/
    fun getContact(contactId: Long) : Contact {
        lateinit var contact : Contact
        database.child("users").child(contactId.toString()).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            contact = Contact(it.value as Contact)
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        return contact
    }


}