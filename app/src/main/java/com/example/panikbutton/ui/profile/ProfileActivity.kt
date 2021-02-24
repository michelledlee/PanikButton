package com.example.panikbutton.ui.profile

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.panikbutton.R
import com.example.panikbutton.data.Contact
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.example.panikbutton.ui.home.HomeActivity
import com.example.panikbutton.ui.home.HomeFragment
import com.example.panikbutton.ui.profile.contacts.*
import com.google.firebase.database.DatabaseReference

const val CONTACT_ID = "contact id"

class ProfileActivity : AppCompatActivity(), ReadAndWriteSnippets {

    private val newContactActivityRequestCode = 1
//    private val contactsListViewModel by viewModels<ContactsViewModel> {
//        ContactsListViewModelFactory(this)
//    }

    override lateinit var database: DatabaseReference
    private lateinit var bottomNav: View
    val contactsAdapter = ContactsAdapter { contact -> adapterOnClick(contact) }
    val headerAdapter = HeaderAdapter()
    val concatAdapter = ConcatAdapter(headerAdapter, contactsAdapter)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
//
//        LocalBroadcastManager.getInstance(this)
//            .registerReceiver(broadCastReceiver, IntentFilter(BROADCAST_DEFAULT_ALBUM_CHANGED))

        val recyclerView: RecyclerView = findViewById(R.id.contacts_recyclerView)
        recyclerView.adapter = concatAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        initializeDbRef()

        val sharedPref =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = getString(R.string.current_user_id )
        val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)
        if (userId != null) {
            getContacts(userId) {
                it.let {
                    contactsAdapter.submitList(it as MutableList<Contact>)
                    headerAdapter.updateContactCount(it.size)
                }
            }
        }
//        contactsListViewModel.contactLiveData.observe(this, {
//            it?.let {
//                contactsAdapter.submitList(it as MutableList<Contact>)
//                headerAdapter.updateContactCount(it.size)
//            }
//        })

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)


        bottomNav = findViewById(R.id.navigation_home)
        bottomNav.setOnClickListener {
//            onBackPressed()
//            val homeFragment = HomeFragment()
//            supportFragmentManager.beginTransaction().add(R.id.home_fragment, homeFragment)
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val addContactButton: View = findViewById(R.id.add_contact)
        addContactButton.setOnClickListener {
            addContactOnClick()
        }
    }

//    override fun onDestroy() {
//        super.onDestroy()
//        LocalBroadcastManager.getInstance(this)
//            .unregisterReceiver(broadCastReceiver)
//    }

//    val broadCastReceiver = object : BroadcastReceiver() {
//        override fun onReceive(contxt: Context?, intent: Intent?) {
//            when (intent?.action) {
//                CONTACT_LIST_CHANGED -> contactsAdapter.notifyDataSetChanged()
//            }
//        }
//    }

    /* Opens EditDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(contact: Contact) {
        val intent = Intent(this, EditContactActivity()::class.java)
        intent.putExtra(CONTACT_ID, contact.id)
        startActivityForResult(intent, newContactActivityRequestCode)
    }

    /* Adds contact to contactList when button is clicked. */
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
                getContacts(userId) {
                    it.let {
                        contactsAdapter.submitList(it as MutableList<Contact>)
                        headerAdapter.updateContactCount(it.size)
                        contactsAdapter.notifyDataSetChanged()
                    }
                }
            }
//            intentData?.let { data ->
//                val contactName = data.getStringExtra(CONTACT_NAME)
//                val contactPhone = data.getLongExtra(CONTACT_PHONE, -1)
//                val contactEmail = data.getStringExtra(CONTACT_EMAIL)
//                contactsListViewModel.insertContact(contactName, contactPhone, contactEmail)
//            }
        }
    }

//    override fun onSupportNavigateUp(): Boolean {
//        try {
//            findNavController(bottomNav).getBackStackEntry(R.id.home_fragment)
//        } catch (ignored: Throwable) {
////            onBackPressed()
//            val homeFragment = HomeFragment()
//            supportFragmentManager.beginTransaction().add(R.id.home_fragment, homeFragment)
//        }
//        onBackPressed()
//        return super.onSupportNavigateUp()
//    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }

}