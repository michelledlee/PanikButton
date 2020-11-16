package com.example.panikbutton.ui.profile.contacts

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R
import com.google.android.material.textfield.TextInputEditText

const val CONTACT_NAME = "name"
const val CONTACT_PHONE = "phone"
const val CONTACT_EMAIL = "email"

class AddContactActivity : AppCompatActivity() {
    private lateinit var addContactName: TextInputEditText
    private lateinit var addContactPhone: TextInputEditText
    private lateinit var addContactEmail: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_contact_layout)

        findViewById<Button>(R.id.done_button).setOnClickListener {
            addFlower()
        }
        addContactName = findViewById(R.id.add_contact_name)
        addContactPhone = findViewById(R.id.add_contact_phone)
        addContactEmail = findViewById(R.id.add_contact_email)

    }

    /* The onClick action for the done button. Closes the activity and returns the new flower name
    and description as part of the intent. If the name or description are missing, the result is set
    to cancelled. */

    private fun addFlower() {
        val resultIntent = Intent()

        if (addContactName.text.isNullOrEmpty() || addContactPhone.text.isNullOrEmpty()) {
            setResult(Activity.RESULT_CANCELED, resultIntent)
        } else {
            val name = addContactName.text.toString()
            val phone = addContactPhone.text.toString()
            val email = addContactEmail.text.toString()
            resultIntent.putExtra(CONTACT_NAME, name)
            resultIntent.putExtra(CONTACT_PHONE, phone)
            resultIntent.putExtra(CONTACT_EMAIL, email)
            setResult(Activity.RESULT_OK, resultIntent)
        }
        finish()
    }
}