package com.example.panikbutton.ui.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
        val panikButton: View = findViewById(R.id.panic_button)
        panikButton.setOnClickListener {
            sendSMS()
            sendEmail()
        }
    }

    private fun sendSMS() {
        // Get list from firebase based on userID
        val phoneNo = ""
        // Iterate through and send text messages
        val smsManager = SmsManager.getDefault() as SmsManager
        smsManager.sendTextMessage(
            phoneNo.toString(), null,
            resources.getString(R.string.default_text_message), null, null
        )
    }

    private fun sendEmail() {
        // Get list from firebase based on userID
        val email = ""
        // Iterate through and send email messages
        val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
            "mailto", email, null))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.default_email_subject))
        emailIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.default_text_message))
        startActivity(Intent.createChooser(emailIntent, resources.getString(R.string.label_send_email)))

    }
}