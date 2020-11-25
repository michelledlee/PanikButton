package com.example.panikbutton.ui.profile.mainProfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.panikbutton.R
import com.example.panikbutton.database.*
import com.example.panikbutton.ui.profile.addContact.AddContactActivity
import com.example.panikbutton.ui.profile.addContact.CONTACT_EMAIL
import com.example.panikbutton.ui.profile.addContact.CONTACT_NAME
import com.example.panikbutton.ui.profile.addContact.CONTACT_PHONE
import com.example.panikbutton.ui.profile.contactDetail.EditContactActivity
//import com.example.panikbutton.ui.profile.contactDetail.ContactDetailActivity
import com.example.panikbutton.ui.profile.contacts.*

const val CONTACT_ID = "contact id"

class ProfileActivity : AppCompatActivity() {

    private val newContactActivityRequestCode = 1
//    private val contactsListViewModel by viewModels<ContactsViewModel> {
//        ContactsListViewModelFactory(this)
//    }
    // Database access
    private lateinit var dataSource : ContactDao
    private lateinit var viewModelFactory : ContactViewModelFactory
    private lateinit var contactViewModel : ContactViewModel


    private lateinit var bottomNav: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dataSource = ContactDatabase.getInstance(this).contactDatabaseDao
        viewModelFactory = ContactViewModelFactory(dataSource, application)
        contactViewModel = ViewModelProvider(this, viewModelFactory).get(ContactViewModel::class.java)

        /* Instantiates headerAdapter and contactsAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val headerAdapter = HeaderAdapter()
        val contactsAdapter = ContactsAdapter { contact -> adapterOnClick(contact) }
        val concatAdapter = ConcatAdapter(headerAdapter, contactsAdapter)

        val recyclerView: RecyclerView = findViewById(R.id.contacts_recyclerView)
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

//        contactsListViewModel.contactLiveData.observe(this, {
//            it?.let {
//                contactsAdapter.submitList(it as MutableList<Contact>)
//                headerAdapter.updateContactCount(it.size)
//            }
//        })

        contactViewModel.contacts.observe(this, {
            it?.let {
                contactsAdapter.submitList(it as MutableList<ContactEntity>)
                headerAdapter.updateContactCount(it.size)
//                Log.e("num contacts:", it.size.toString())
            }
        })

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        bottomNav = findViewById(R.id.navigation_home)
        bottomNav.setOnClickListener {
            onBackPressed()
        }

        val addContactButton: View = findViewById(R.id.add_contact)
        addContactButton.setOnClickListener {
            addContactOnClick()
        }

    }

    /* Opens EditDetailActivity when RecyclerView item is clicked. */
//    private fun adapterOnClick(contact: Contact) {
    private fun adapterOnClick(contact: ContactEntity) {
        val intent = Intent(this, EditContactActivity()::class.java)
        intent.putExtra(CONTACT_ID, contact.id)
        startActivityForResult(intent, newContactActivityRequestCode)
    }

    /* Adds contact to contactList when button is clicked. */
    private fun addContactOnClick() {
        val intent = Intent(this, AddContactActivity::class.java)
        startActivityForResult(intent, newContactActivityRequestCode)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
//        super.onActivityResult(requestCode, resultCode, intentData)
//        // Inserts contact into viewModel.
//        if (requestCode == newContactActivityRequestCode && resultCode == Activity.RESULT_OK) {
//            intentData?.let { data ->
//                val contactName = data.getStringExtra(CONTACT_NAME)
//                val contactPhone = data.getLongExtra(CONTACT_PHONE, -1)
//                val contactEmail = data.getStringExtra(CONTACT_EMAIL)
//                contactsListViewModel.insertContact(contactName, contactPhone, contactEmail)
//            }
//        }
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        // Inserts contact into viewModel.
        if (requestCode == newContactActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val contactName = data.getStringExtra(CONTACT_NAME)
                val contactPhone = data.getLongExtra(CONTACT_PHONE, -1)
                val contactEmail = data.getStringExtra(CONTACT_EMAIL)
//                viewModelFactory.insertContact(contactName, contactPhone, contactEmail)
                if (contactName != null && contactEmail != null) {
                    contactViewModel.insertContact(contactName, contactPhone, contactEmail)
                    Log.e("New contact", "added from onActivityResult")
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}