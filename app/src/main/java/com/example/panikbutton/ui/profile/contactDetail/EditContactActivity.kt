package com.example.panikbutton.ui.profile.contactDetail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R
import com.example.panikbutton.ui.profile.addContact.CONTACT_EMAIL
import com.example.panikbutton.ui.profile.addContact.CONTACT_NAME
import com.example.panikbutton.ui.profile.addContact.CONTACT_PHONE
import com.google.android.material.textfield.TextInputEditText

class EditContactActivity : AppCompatActivity() {
    private lateinit var editContactName: TextInputEditText
    private lateinit var editContactPhone: TextInputEditText
    private lateinit var editContactEmail: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_contact_layout)

        findViewById<Button>(R.id.save_contact_profile).setOnClickListener {
            editContact()
        }
        findViewById<Button>(R.id.delete_contact_button).setOnClickListener {
            deleteContact()
        }
        editContactName = findViewById(R.id.edit_contact_name)
        editContactPhone = findViewById(R.id.edit_contact_phone)
        editContactEmail = findViewById(R.id.edit_contact_email)

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

    private fun deleteContact() {

    }
}