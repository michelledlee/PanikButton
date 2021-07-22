package com.example.panikbutton.ui.profile.contacts

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.panikbutton.R
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import kotlin.random.Random
import com.example.panikbutton.databinding.ActivityAddcontactBinding
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

const val CONTACT_NAME = "name"
const val CONTACT_PHONE = "phone"
const val CONTACT_EMAIL = "email"

class AddContactActivity : AppCompatActivity(), ReadAndWriteSnippets {
    private lateinit var addContactName: TextInputEditText
    private lateinit var addContactPhone: TextInputEditText
    private lateinit var addContactEmail: TextInputEditText

    override lateinit var database: DatabaseReference

    private lateinit var binding: ActivityAddcontactBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddcontactBinding.inflate(layoutInflater)
        setContentView(binding.root)

        findViewById<Button>(R.id.done_button).setOnClickListener {
            if (addContact()) {
                finish()
            }
        }
        addContactName = findViewById(R.id.add_contact_name)
        addContactPhone = findViewById(R.id.add_contact_phone)
        addContactEmail = findViewById(R.id.add_contact_email)

        // Initialize Firebase
        initializeDbRef()

    }

    /** The onClick action for the done button. Closes the activity and returns the new contact name
    and description as part of the intent. If the name or description are missing, the result is set
    to cancelled. **/
    private fun addContact(): Boolean {
        val resultIntent = Intent()

        if (addContactName.text.isNullOrEmpty() || addContactPhone.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val name = addContactName.text.toString()
            val phone = addContactPhone.text.toString()
            val email = addContactEmail.text.toString()

            /* Form validation */
            // Validation for phone number
            if (!validatePhone(phone)) {
                return false
            }
            // Validation for email
            if (!email.isEmailValid()) {
                return false
            }

            /* Save to database */
            // Generate contact id
            val contactId = Random.nextInt()
            // Create new contact
            getUserId()?.let { getUserId()?.let { it1 -> writeNewContact(it1, contactId, name, phone.toLong(), email) } }

            // Pass intent
            resultIntent.putExtra(CONTACT_NAME, name)
            resultIntent.putExtra(CONTACT_PHONE, phone)
            resultIntent.putExtra(CONTACT_EMAIL, email)
            setResult(Activity.RESULT_OK, resultIntent)
        }

        return true
    }

    /** Get userId **/
    private fun getUserId() : String? {
        val sharedPref = this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = resources.getString(R.string.current_user_id)
        return sharedPref.getString(getString(R.string.current_user_id), defaultValue)
    }

    /** Validate phone number so it can be formatted correctly in UI **/
    private fun validatePhone(inputtedPhone: String): Boolean {
        var valid = true

        // Check if number is empty
        if (inputtedPhone.isEmpty()) {
            binding.addContactPhone.error = "Required."
            valid = false
        }

        // Check length of number
        if (inputtedPhone.length != 10) {
            // Prevent submission until user enters a proper phone number
            binding.addContactPhone.error = "Enter a valid phone number."
            valid = false
        }

        // Check if all digits
        if (!inputtedPhone.isDigitsOnly()) {
            binding.addContactPhone.error = "Enter valid phone (without () or -)."
            valid = false
        }

        return valid
    }

    /** Validate email so it can be formatted correctly in UI **/
    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
    }
}