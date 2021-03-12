package com.example.panikbutton.ui.home

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.view.View
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
                showInContextUI(...)
            }
                else -> {
                    // Request SMS permissions from the user
                    requestPermissions(arrayOf(android.Manifest.permission.SEND_SMS),
                        REQUEST_CODE)
                }
            }


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
//                    val uri = Uri.parse(item.contactPhone.toString())
//                    val intent = Intent(Intent.ACTION_SENDTO, uri)
//                    intent.putExtra("sms_body", resources.getString(R.string.default_text_message))
//                    if (intent.resolveActivity(packageManager) != null) {
//                        startActivity(intent)
//                    }
                    val smsManager = SmsManager.getDefault() as SmsManager
                    smsManager.sendTextMessage(item.contactPhone.toString(), null, resources.getString(R.string.default_text_message), null, null)

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