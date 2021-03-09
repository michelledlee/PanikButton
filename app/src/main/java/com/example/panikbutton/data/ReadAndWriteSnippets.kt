package com.example.panikbutton.data

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
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
    fun getContact(userId: String, contactId: String, callback: (contact: Contact) -> Unit ) {
        database.child("users").child(userId).child("contacts").child(contactId).get().addOnSuccessListener { it ->
            Log.i("firebase", "Got value ${it.value}")
            it.getValue(Contact::class.java)?.let {
                callback(Contact(it.id, it.contactName, it.contactPhone, it.contactEmail))
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    /** Gets current list of contacts **/
    fun getContacts(userId: String, callback: (list: List<Contact>) -> Unit) {
        database.child("users").child(userId).child("contacts").addListenerForSingleValueEvent(
            object : ValueEventListener {
                // Parse each of the children into a list
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.e("contacts", snapshot.toString())
                    if (snapshot.exists()) {
                        val list: MutableList<Contact> = mutableListOf()
                        val children = snapshot.children
                        children.forEach {
                            it.getValue(Contact::class.java)?.let {
                                    it1 -> list.add(Contact(it1.id, it1.contactName, it1.contactPhone, it1.contactEmail))
                            }
                        }
                        callback(list)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("getContacts", "failed to read value")
                }
            })

    }

    /** Save edited existing contact **/
    fun saveContact(userId: String, contactId: Int, contactName: String, contactPhone: Long, contactEmail: String) {
        val contact = Contact(contactId, contactName, contactPhone, contactEmail)
        database.child("users").child(userId).child("contacts").child(contactId.toString()).setValue(contact)
            .addOnSuccessListener {
                // Write was successful!
                Log.e("saveContact", "Success!")
            }
            .addOnFailureListener {
                // Write failed
                Log.e("saveContact", "Failed :(")
            }
    }

    /** Deletes a contact from Firebase **/
    fun deleteContactFromFirebase(userId: String, contactId: String) {
        database.child("users").child(userId).child("contacts").child(contactId).removeValue()
            .addOnSuccessListener { Log.d("EditContactActivity", "Contact $userId successfully deleted!") }
            .addOnFailureListener { e -> Log.w("EditContactActivity", "Error deleting contact", e) }
    }


}