//package com.example.panikbutton.data
//
//import android.content.Context
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import com.example.panikbutton.MainActivity
//import com.example.panikbutton.R
////import com.example.panikbutton.data.Contacts
//import com.google.firebase.database.DatabaseReference
//import com.google.firebase.database.ktx.database
//import com.google.firebase.ktx.Firebase
//
//class DataSource: ViewModel(), ReadAndWriteSnippets {
//
//    private val initialContactList = contactList()
//    private val contactsLiveData = MutableLiveData(initialContactList)
//    override lateinit var database: DatabaseReference
//
////    private fun contactList(): List<Contact> {
////        database = Firebase.database.reference
////        lateinit var thelist: List<Contact>
////
////        val value =
////        val defaultValue = MainActivity.Strings.get(R.string.current_user_id)
////        val userId = MainActivity.sharedPreferences.getString(
////            MainActivity.Strings.get(R.string.current_user_id),
////            defaultValue
////        )
////        if (userId != null) {
////            getContacts(userId) {
////                thelist = it
////            }
////        }
////        return thelist
////    }
//
//    /* Adds contact to liveData and posts value. */
//    fun addContact(contact: Contact) {
//        val currentList = contactsLiveData.value
//        if (currentList == null) {
//            contactsLiveData.postValue(listOf(contact))
//        } else {
//            val updatedList = currentList.toMutableList()
//            updatedList.add(0, contact)
//            contactsLiveData.postValue(updatedList)
//        }
//    }
//
//    /* Removes contact from liveData and posts value. */
//    fun removeContact(contact: Contact) {
//        val currentList = contactsLiveData.value
//        if (currentList != null) {
//            val updatedList = currentList.toMutableList()
//            updatedList.remove(contact)
//            contactsLiveData.postValue(updatedList)
//        }
//    }
//
//    /* Returns contact given an ID. */
//    fun getContactForId(id: Int): Contact? {
//        contactsLiveData.value?.let { contacts ->
//            return contacts.firstOrNull{ it.id == id}
//        }
//        return null
//    }
//
//    fun getContactList(): LiveData<List<Contact>> {
//        return contactsLiveData
//    }
//
//    companion object {
//        private var INSTANCE: DataSource? = null
//
//        fun getDataSource(): DataSource {
//            return synchronized(DataSource::class) {
//                val newInstance = INSTANCE ?: DataSource()
//                INSTANCE = newInstance
//                newInstance
//            }
//        }
//    }
//}