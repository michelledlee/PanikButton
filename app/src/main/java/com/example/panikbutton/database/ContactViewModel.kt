package com.example.panikbutton.database

import android.app.Application
import android.os.AsyncTask
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.panikbutton.data.Contact
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ContactViewModel(private val database: ContactDao, application: Application) : AndroidViewModel(application) {

    var contacts = MutableLiveData<List<ContactEntity>>()

    init {
        initializeContacts()
    }

    private fun initializeContacts() {
        viewModelScope.launch {
            contacts.value = getContactsFromDatabase()
        }
    }

    private fun getContactsFromDatabase() : List<ContactEntity>? {
        return database.getAll().value
    }

    fun insertContact(contactName: String, contactPhone: Long, contactEmail: String) {
        viewModelScope.launch {
            val newContact = ContactEntity(contactName = contactName, contactPhone = contactPhone, contactEmail = contactEmail)
            insert(newContact)
        }
    }

    fun deleteContact(contact: ContactEntity) {
        viewModelScope.launch {
            deleteContact(contact)
        }
    }

    fun getContactForId(id: Long) : ContactEntity? {
        return database.getContactForId(id)
    }


    private suspend fun insert(contact: ContactEntity) {
        database.insert(contact)
    }

}