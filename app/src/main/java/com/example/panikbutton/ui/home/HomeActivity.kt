package com.example.panikbutton.ui.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.panikbutton.R
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.google.firebase.database.DatabaseReference
import java.util.jar.Manifest

const val REQUEST_CODE = 1


class HomeActivity : AppCompatActivity(), ReadAndWriteSnippets {
    override lateinit var database: DatabaseReference

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        initializeDbRef()

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
        val panikButton: View = findViewById(R.id.panic_button)
        panikButton.setOnClickListener {

            when {
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    sendSMS()
                }

                shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
//                showInContextUI(...)
                }
                else -> {
                    // Request SMS permissions from the user
                    requestPermissions(arrayOf(android.Manifest.permission.SEND_SMS),
                        REQUEST_CODE)
                }
            }


//            sendEmail()
        }
    }

    /** Sends an SMS to each contact in the user's contact list **/
    private fun sendSMS() {
        val sharedPref =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = getString(R.string.current_user_id )
        val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)
        val userPhone = sharedPref.getString(getString(R.string.current_phone_name), defaultValue)
        val userName = sharedPref.getString(getString(R.string.current_user_name), userPhone)
        if (userId != null) {
            // Get list from firebase based on userID
            getContacts(userId) { it ->
                for (item in it) {
                    // Iterate through and send text messages
                    val smsManager: SmsManager = SmsManager.getDefault()
                    val phoneNumber: String = item.contactPhone.toString()
                    val contactName: String = item.contactName
                    val smsMessage: String = String.format(resources.getString(R.string.default_text_message), userName)
                    if (TextUtils.isDigitsOnly(phoneNumber)) {
                        smsManager.sendTextMessage(phoneNumber, null, smsMessage, null, null)
                        val verificationMsg: String = String.format(resources.getString(R.string.message_sent), contactName)
                        Toast.makeText(this, verificationMsg, Toast.LENGTH_SHORT).show()
                    } else {
                        val message : String = String.format(getString(R.string.invalid_phone_number), contactName)
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }

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