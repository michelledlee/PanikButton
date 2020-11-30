package com.example.panikbutton.data

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataSource(resources: Resources): ViewModel() {

    private val initialContactList = contactList()
    private val contactsLiveData = MutableLiveData(initialContactList)

    /* Adds contact to liveData and posts value. */
    fun addContact(contact: Contact) {
        val currentList = contactsLiveData.value
        if (currentList == null) {
            contactsLiveData.postValue(listOf(contact))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, contact)
            contactsLiveData.postValue(updatedList)
        }
    }

    /* Removes contact from liveData and posts value. */
    fun removeContact(contact: Contact) {
        val currentList = contactsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(contact)
            contactsLiveData.postValue(updatedList)
        }
    }

    /* Returns contact given an ID. */
    fun getContactForId(id: Long): Contact? {
        contactsLiveData.value?.let { contacts ->
            return contacts.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getContactList(): LiveData<List<Contact>> {
        return contactsLiveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(resources: Resources): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(resources)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}