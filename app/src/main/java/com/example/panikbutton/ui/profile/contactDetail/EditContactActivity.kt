package com.example.panikbutton.ui.profile.contactDetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R
import com.example.panikbutton.data.Contact
import com.example.panikbutton.ui.profile.addContact.CONTACT_EMAIL
import com.example.panikbutton.ui.profile.addContact.CONTACT_NAME
import com.example.panikbutton.ui.profile.addContact.CONTACT_PHONE
import com.example.panikbutton.ui.profile.mainProfile.CONTACT_ID
import com.google.android.material.textfield.TextInputEditText

/* Opened from the ContactsAdapter.kt class when a user clicks on a RecyclerView item*/
class EditContactActivity : AppCompatActivity() {
    private lateinit var editContactName: TextInputEditText
    private lateinit var editContactPhone: TextInputEditText
    private lateinit var editContactEmail: TextInputEditText

    private val contactDetailViewModel by viewModels<ContactDetailViewModel> {
        ContactDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_contact_layout)

        var currentContactId: Long? = null

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentContactId = bundle.getLong(CONTACT_ID)
        }

        Log.e("currentContactId", CONTACT_ID)

        editContactName = findViewById(R.id.edit_contact_name)
        editContactPhone = findViewById(R.id.edit_contact_phone)
        editContactEmail = findViewById(R.id.edit_contact_email)

        /* If currentContactId is not null, get corresponding contact details */
        currentContactId?.let {
            val currentContact = contactDetailViewModel.getContactForId(it)

            editContactName.setText(currentContact?.contactName)
            editContactPhone.setText(currentContact?.contactPhone.toString())
            editContactEmail.setText(currentContact?.contactEmail)

            findViewById<Button>(R.id.delete_contact_button).setOnClickListener {
                if (currentContact != null) {
                    deleteContact(currentContact)
                }
            }
        }

        findViewById<Button>(R.id.save_contact_profile).setOnClickListener {
            editContact()
        }

    }

    /* The onClick action for the done button. Closes the activity and returns the new contact name
    and description as part of the intent. If the name or description are missing, the result is set
    to cancelled. */
    private fun editContact() {
        val resultIntent = Intent()

        if (editContactName.text.isNullOrEmpty() || editContactPhone.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val name = editContactName.text.toString()
            val phone = editContactPhone.text.toString()
            val email = editContactEmail.text.toString()
            resultIntent.putExtra(CONTACT_NAME, name)
            resultIntent.putExtra(CONTACT_PHONE, phone)
            resultIntent.putExtra(CONTACT_EMAIL, email)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }

    private fun deleteContact(currentContact : Contact) {
        contactDetailViewModel.removeContact(currentContact)
        finish()
    }
}