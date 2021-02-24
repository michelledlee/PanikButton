//package com.example.panikbutton.data
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import com.example.panikbutton.MainActivity
////import com.example.panikbutton.MainActivity.Companion.sharedPreferences
//import com.example.panikbutton.R
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//
////class Contacts : AppCompatActivity(), ReadAndWriteSnippets {
//////object Contacts : AppCompatActivity(), ReadAndWriteSnippets {
////    override lateinit var database: DatabaseReference
////
////    override fun onCreate(savedInstanceState: Bundle?) {
////        super.onCreate(savedInstanceState)
////        database = Firebase.database.reference
////    }
//
//    /* Returns initial list of contacts. */
//    fun contactList(): List<Contact> {
////        lateinit var thelist: List<Contact>
////
////        val defaultValue = MainActivity.Strings.get(R.string.current_user_id)
////        val userId = sharedPreferences.getString(MainActivity.Strings.get(R.string.current_user_id), defaultValue)
////        if (userId != null) {
////            getContacts(userId) {
////                thelist = it
////            }
////        }
////        return thelist
//        return listOf(
//            Contact(
//                id = 1,
//                contactName = "Contact1",
//                contactPhone = 9055097899,
//                contactEmail = "Contact1@email.com",
//            ),
//            Contact(
//                id = 2,
//                contactName = "Contact2",
//                contactPhone = 8185939830,
//                contactEmail = "Contact2@email.com",
//            )
//        )
//    }
////}