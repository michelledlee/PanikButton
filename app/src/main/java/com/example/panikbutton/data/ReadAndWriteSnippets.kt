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
    fun writeNewUser(userId: String, userName: String, userPhone: Long, userEmail: String) {
        val user = User(userId, userName, userPhone, userEmail)
        database.child("users").child(userId.toString()).setValue(user)

    }

    /** Edit existing contact**/
    fun editUser(userId: String, userName: String, userPhone: Long, userEmail: String) {
        val user = User(userId, userName, userPhone, userEmail)
        database.child("users").child(userId.toString()).setValue(user)

    }

    /** Add a new user to the database with success/failure listeners **/
    fun writeNewUserWithTaskListeners(userName: String, userPhone: Long, userEmail: String) {
        val userId = Random.nextLong()
        val user = User(userId.toString(), userName, userPhone, userEmail)

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
    fun writeNewContact(userId: String, contactId: Int, contactName: String, contactPhone: Long, contactEmail: String) {
        val contact = Contact(contactId, contactName, contactPhone, contactEmail)

        Log.e("firebase", "FROM WRITE NEW CONTACT")

        database.child("users").child(userId).child("contacts").child(contactId.toString()).setValue(contact)
            .addOnSuccessListener {
                // Write was successful!
                Log.e("firebase", "SUCCESSFUL WRITE")
            }
            .addOnFailureListener {
                // Write failed
                Log.e("firebase", "FAILED WRITE")
            }
    }

    /** Get contact based on ID **/
    fun getContact(contactId: String) : Contact {
        lateinit var contact : Contact
        database.child("users").child(contactId).get().addOnSuccessListener {
            Log.i("firebase", "Got value ${it.value}")
            contact = Contact(it.value as Contact)
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }

        return contact
    }


}