package com.example.panikbutton.ui.profile.mainProfile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.panikbutton.R
import com.example.panikbutton.data.Contact
import com.example.panikbutton.ui.profile.addContact.AddContactActivity
import com.example.panikbutton.ui.profile.addContact.CONTACT_EMAIL
import com.example.panikbutton.ui.profile.addContact.CONTACT_NAME
import com.example.panikbutton.ui.profile.addContact.CONTACT_PHONE
import com.example.panikbutton.ui.profile.contactDetail.ContactDetailActivity
import com.example.panikbutton.ui.profile.contacts.*

const val CONTACT_ID = "contact id"

class ProfileActivity : AppCompatActivity() {

    private val newContactActivityRequestCode = 1
    private val contactsListViewModel by viewModels<ContactsViewModel> {
        ContactsListViewModelFactory(this)
    }

    private lateinit var bottomNav: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        /* Instantiates headerAdapter and contactsAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val headerAdapter = HeaderAdapter()
        val contactsAdapter = ContactsAdapter { contact -> adapterOnClick(contact) }
        val concatAdapter = ConcatAdapter(headerAdapter, contactsAdapter)

        val recyclerView: RecyclerView = findViewById(R.id.contacts_recyclerView)
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        contactsListViewModel.contactLiveData.observe(this, {
            it?.let {
                contactsAdapter.submitList(it as MutableList<Contact>)
                headerAdapter.updateContactCount(it.size)
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

    /* Opens ContactDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(contact: Contact) {
        val intent = Intent(this, ContactDetailActivity()::class.java)
        intent.putExtra(CONTACT_ID, contact.id)
        startActivity(intent)
    }

    /* Adds contact to contactList when button is clicked. */
    private fun addContactOnClick() {
        val intent = Intent(this, AddContactActivity::class.java)
        startActivityForResult(intent, newContactActivityRequestCode)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
        /* Inserts contact into viewModel. */
        if (requestCode == newContactActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val contactName = data.getStringExtra(CONTACT_NAME)
                val contactPhone = data.getIntExtra(CONTACT_PHONE, -1)
                val contactEmail = data.getStringExtra(CONTACT_EMAIL)
                contactsListViewModel.insertContact(contactName, contactPhone, contactEmail)
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