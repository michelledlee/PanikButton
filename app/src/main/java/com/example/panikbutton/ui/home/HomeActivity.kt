package com.example.panikbutton.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.google.firebase.database.DatabaseReference

class HomeActivity : AppCompatActivity(), ReadAndWriteSnippets {
    override lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        initializeDbRef()

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
        val panikButton: View = findViewById(R.id.panic_button)
        panikButton.setOnClickListener {
            sendSMS()
            sendEmail()
        }
    }

    /** Sends an SMS to each contact in the user's contact list **/
    private fun sendSMS() {
        val sharedPref =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = getString(R.string.current_user_id )
        val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)
        if (userId != null) {
            // Get list from firebase based on userID
            getContacts(userId) { it ->
                for (item in it) {
                    // Iterate through and send text messages
                    val uri = Uri.parse(item.contactPhone.toString())
                    val intent = Intent(Intent.ACTION_SENDTO, uri)
                    intent.putExtra("sms_body", resources.getString(R.string.default_text_message))
                    startActivity(intent)
                }
            }
        }
    }

    /** Sends an email to each contact in the user's contact list **/
    private fun sendEmail() {
        val sharedPref =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = getString(R.string.current_user_id )
        val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)
        if (userId != null) {
            // Get list from firebase based on userID
            getContacts(userId) {
                for (item in it) {
                    // Iterate through and send text messages
                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", item.contactEmail, null))
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.default_email_subject))
                    emailIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.default_text_message))
                    startActivity(Intent.createChooser(emailIntent, resources.getString(R.string.label_send_email)))
                }
            }
        }

    }
}