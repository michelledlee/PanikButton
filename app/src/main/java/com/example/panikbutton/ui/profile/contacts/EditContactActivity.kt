package com.example.panikbutton.ui.profile.contacts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.panikbutton.R
import com.example.panikbutton.data.Contact
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.example.panikbutton.ui.profile.CONTACT_ID
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference

/* Opened from the ContactsAdapter.kt class when a user clicks on a RecyclerView item*/
class EditContactActivity : AppCompatActivity(), ReadAndWriteSnippets {
    private lateinit var editContactName: TextInputEditText
    private lateinit var editContactPhone: TextInputEditText
    private lateinit var editContactEmail: TextInputEditText
    private lateinit var userId : String
    private lateinit var currentContactId: String
    override lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_contact_layout)

//        var currentContactId: String? = null

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentContactId = bundle.getString("contact id").toString()
        }

        // Getting fields to pull user data from
        editContactName = findViewById(R.id.edit_contact_name)
        editContactPhone = findViewById(R.id.edit_contact_phone)
        editContactEmail = findViewById(R.id.edit_contact_email)

        // Need database reference to get contact details
        initializeDbRef()

        // Need a reference to the userID to get contacts
        val sharedPref =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = getString(R.string.current_user_id )
         userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue).toString()

        // Populating fields with the contact detail
        if (currentContactId != null) {
            getContact(userId.toString(), currentContactId) {
                it.let {
                    editContactName.setText(it.contactName)
                    editContactPhone.setText(it.contactPhone.toString())
                    editContactEmail.setText(it.contactEmail)
                }
            }
        }

        // Hooking up save button
        findViewById<Button>(R.id.save_contact_profile).setOnClickListener {
            if (editContact()) {
                finish()
            }
        }

        /* If currentContactId is not null, get corresponding contact details */
        currentContactId?.let {
            findViewById<Button>(R.id.delete_contact_button).setOnClickListener {
                deleteContactFromFirebase(currentContactId)
                finish()
            }
        }
    }

    /** The onClick action for the done button. Closes the activity and returns the new contact name
    and description as part of the intent. If the name or description are missing, the result is set
    to cancelled. **/
    private fun editContact(): Boolean {
        val resultIntent = Intent()
        if (editContactName.text.isNullOrEmpty() || editContactPhone.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val name = editContactName.text.toString()
            val phone = editContactPhone.text.toString()

            // Validation for phone number
            if (!validatePhone(phone)) {
                return false
            }
            val email = editContactEmail.text.toString()
            // Validation for email
            if (!email.isEmailValid()) {
                return false
            }

            saveContact(userId, currentContactId.toInt(), name, phone.toLong(), email)

            resultIntent.putExtra(CONTACT_NAME, name)
            resultIntent.putExtra(CONTACT_PHONE, phone.toLong())
            resultIntent.putExtra(CONTACT_EMAIL, email)
            setResult(Activity.RESULT_OK, resultIntent)
        }

        return true

    }

    /** Validate phone number so it can be formatted correctly in UI **/
    private fun validatePhone(inputtedPhone: String): Boolean {
        var valid = true

        val textView : TextInputEditText = findViewById<TextInputEditText>(R.id.edit_contact_phone)

        // Check if number is empty
        if (inputtedPhone.isEmpty()) {
            textView.error = "Required."
            valid = false
        }

        // Check length of number
        if (inputtedPhone.length != 10) {
            textView.error = "Enter a valid phone number."
            valid = false
        }

        // Check if all digits
        if (!inputtedPhone.isDigitsOnly()) {
            textView.error = "Enter valid phone (without () or -)."
            valid = false
        }

        return valid
    }

    /** Validate email so it can be formatted correctly in UI **/
    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }

}