package com.example.panikbutton.ui.profile.contactDetail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.panikbutton.data.Contact
import com.example.panikbutton.data.DataSource

class ContactDetailViewModel(private val dataSource: DataSource) : ViewModel() {
    /* Queries datasource to return a contact that corresponds to an id. */
    fun getContactForId(id: Long) : Contact? {
        return dataSource.getContactForId(id)
    }

    /* Queries datasource to remove a contact. */
    fun removeContact(contact: Contact) {
        dataSource.removeContact(contact)
    }

}

class ContactDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactDetailViewModel(
                dataSource = DataSource.getDataSource(context.resources)
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}