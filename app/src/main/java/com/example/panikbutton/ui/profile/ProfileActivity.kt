package com.example.panikbutton.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.panikbutton.R
import com.example.panikbutton.data.Contact
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.example.panikbutton.ui.about.AboutActivity
import com.example.panikbutton.ui.home.HomeActivity
import com.example.panikbutton.ui.profile.contacts.AddContactActivity
import com.example.panikbutton.ui.profile.contacts.ContactsAdapter
import com.example.panikbutton.ui.profile.contacts.EditContactActivity
import com.example.panikbutton.ui.profile.contacts.HeaderAdapter
import com.example.panikbutton.ui.settings.SettingsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference

const val CONTACT_ID = "contact id"

class ProfileActivity : AppCompatActivity(), ReadAndWriteSnippets {

    private val newContactActivityRequestCode = 1
    override lateinit var database: DatabaseReference
    private lateinit var bottomNav: View
    private val contactsAdapter = ContactsAdapter { contact -> adapterOnClick(contact) }
    private val headerAdapter = HeaderAdapter()
    private val concatAdapter = ConcatAdapter(headerAdapter, contactsAdapter)

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        initializeDbRef()   // Initialize Firebase reference

        // Initialize Recyclerview for contacts list
        val recyclerView: RecyclerView = findViewById(R.id.contacts_recyclerView)
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Get user details to retrieve contacts list from Firebase
        val sharedPref =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = getString(R.string.current_user_id )
        val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)
        if (userId != null) {
            getContacts(userId) { it ->
                it.let { it ->
                    contactsAdapter.submitList(it.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.contactName })) as MutableList<Contact>)
                    headerAdapter.updateContactCount(it.size)
                }
            }
        }

        // Hook up home button
        bottomNav = findViewById(R.id.navigation_home)
        bottomNav.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        // Hook up add contact button
        val addContactButton: View = findViewById(R.id.add_contact)
        addContactButton.setOnClickListener {
            addContactOnClick()
        }
    }

    /** Create options menu **/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    /** Setting up menu options **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_settings -> {
                launchSettings()
                true
            }
            R.id.action_home -> {
                launchHome()
                true
            }
            R.id.action_about -> {
                launchAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Brings user to the Profile screen via menu selection  **/
    private fun launchSettings() {
        val intent = Intent (this, SettingsActivity::class.java)
        startActivity(intent)
    }

    /** Brings user to the Home screen via menu selection **/
    private fun launchHome() {
        val intent = Intent (this, HomeActivity::class.java)
        startActivity(intent)
    }

    /** Brings user to the About screen via menu selection **/
    private fun launchAbout() {
        val intent = Intent (this, AboutActivity::class.java)
        startActivity(intent)
    }

    /** Opens EditDetailActivity when RecyclerView item is clicked. **/
    private fun adapterOnClick(contact: Contact) {
        val intent = Intent(this, EditContactActivity()::class.java)
        intent.putExtra(CONTACT_ID, contact.id.toString())
        Log.e("from profile activity", contact.id.toString())
        startActivityForResult(intent, newContactActivityRequestCode)
    }

    /** Adds contact to contactList when button is clicked. **/
    private fun addContactOnClick() {
        val intent = Intent(this, AddContactActivity::class.java)
        startActivityForResult(intent, newContactActivityRequestCode)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)
//         Inserts contact into viewModel.
        if (requestCode == newContactActivityRequestCode && resultCode == Activity.RESULT_OK) {
            Log.e("onActivityResult", requestCode.toString())
            val sharedPref =
                this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
            val defaultValue = getString(R.string.current_user_id )
            val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)
            if (userId != null) {
                getContacts(userId) { it ->
                    it.let { it ->
                        contactsAdapter.submitList(it.sortedWith(compareBy(String.CASE_INSENSITIVE_ORDER, { it.contactName })) as MutableList<Contact>)
                        headerAdapter.updateContactCount(it.size)
                        contactsAdapter.notifyDataSetChanged()
                    }
                }
            }
        }
    }

}