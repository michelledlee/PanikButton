package com.example.panikbutton.database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ContactViewModel(private val database: ContactDao,
                       application: Application) : AndroidViewModel(application) {

    var contacts = MutableLiveData<LiveData<List<ContactEntity>>>()

    init {
        initializeContacts()
    }

    private fun initializeContacts() {
        viewModelScope.launch {
            contacts.value = getContactsFromDatabase()
        }
    }

    fun getContactsFromDatabase() : LiveData<List<ContactEntity>> {
        return database.getAll()
    }

    fun insertContact(contactName: String, contactPhone: Long, contactEmail: String) {
        viewModelScope.launch {
            val newContact = ContactEntity(contactName = contactName, contactPhone = contactPhone, contactEmail = contactEmail)
            insert(newContact)
        }
    }

    private suspend fun insert(contact: ContactEntity) {
        database.insert(contact)
    }

}