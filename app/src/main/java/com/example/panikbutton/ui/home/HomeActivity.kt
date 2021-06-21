package com.example.panikbutton.ui.home

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.panikbutton.R
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import java.util.jar.Manifest

const val REQUEST_CODE = 1


class HomeActivity : AppCompatActivity(), ReadAndWriteSnippets {
    override lateinit var database: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        initializeDbRef()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // Check permissions
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)  != PackageManager.PERMISSION_GRANTED||
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS) || shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS)) {
                showInContextUISMS()
                showInContextUILocation()
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
            }
        }
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
        val panikButton: View = findViewById(R.id.panic_button)
        panikButton.setOnClickListener {

            sendSMS()
//            lastLocation()
//            // Check for SMS permissions when using this app
//            when { // Ensure that permission has been granted
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED -> {
//                    sendSMS()
//                }
//                shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS) -> { // Explain why SMS is needed
//                    showInContextUISMS()
//                }
//                else -> { // Request SMS permissions from the user
//                    requestPermissions(arrayOf(android.Manifest.permission.SEND_SMS), REQUEST_CODE)
//                }
//            }
//
//            // Check for location permissions when using this app
//            when { // Ensure that permission has been granted
//                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED -> {
//                    lastLocation()
//                }
//                shouldShowRequestPermissionRationale(android.Manifest.permission.SEND_SMS) -> { // Explain why location is needed
//                    showInContextUILocation()
//                }
//                else -> { // Request SMS permissions from the user
//                    requestPermissions(arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), REQUEST_CODE)
//                }
//            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /** Displays dialog explaining why the SMS permissions is needed **/
    private fun showInContextUISMS() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sms_permissions_layout)
        val okayButton = dialog.findViewById<Button>(R.id.okay_sms_perm)
        okayButton.setOnClickListener() {
            dialog.dismiss()
        }
        val cancelButton = dialog.findViewById<Button>(R.id.back_sms_perm)
        cancelButton.setOnClickListener() {
            dialog.dismiss()
        }
    }

    /** Displays dialog explaining why the SMS permissions is needed **/
    private fun showInContextUILocation() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.background_location_layout)
        val okayButton = dialog.findViewById<Button>(R.id.okay_bg_perm)
        okayButton.setOnClickListener() {
            dialog.dismiss()
        }
        val cancelButton = dialog.findViewById<Button>(R.id.back_bg_perm)
        cancelButton.setOnClickListener() {
            dialog.dismiss()
        }
    }

    /** Sends an SMS to each contact in the user's contact list **/
    @SuppressLint("MissingPermission")
    private fun sendSMS() {
        val sharedPref =
            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = getString(R.string.current_user_id )
        val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)
        val userPhone = sharedPref.getString(getString(R.string.current_phone_name), defaultValue)
        val userName = sharedPref.getString(getString(R.string.current_user_name), userPhone)
        if (userId != null) {
            // Get list from firebase based on userID
            var coord: String = ""
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location? ->
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        coord = String.format(resources.getString(R.string.coordinates), location.latitude, location.longitude)
                        Log.e("coordinates:", coord)
                    }
                }
            getContacts(userId) { it ->
                for (item in it) {
                    // Iterate through and send text messages
                    val smsManager: SmsManager = SmsManager.getDefault()
                    val phoneNumber: String = item.contactPhone.toString()
                    val contactName: String = item.contactName
                    val smsMessage: String = String.format(resources.getString(R.string.default_text_message), userName, coord)

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

//    /** Get the users last known location **/
//    @SuppressLint("MissingPermission")
//    @RequiresApi(Build.VERSION_CODES.M)
//    private fun lastLocation() {
////        if (ActivityCompat.checkSelfPermission(
////                this,
////                android.Manifest.permission.ACCESS_FINE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
////                this,
////                android.Manifest.permission.ACCESS_COARSE_LOCATION
////            ) != PackageManager.PERMISSION_GRANTED
////        ) {
////            // TODO: Consider calling
////            //    ActivityCompat#requestPermissions
////            // here to request the missing permissions, and then overriding
////            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
////            //                                          int[] grantResults)
////            // to handle the case where the user grants the permission. See the documentation
////            // for ActivityCompat#requestPermissions for more details.
////            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION), REQUEST_CODE)
////            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_CODE)
////            return
////        }
//        fusedLocationClient.lastLocation
//            .addOnSuccessListener { location : Location? ->
//                // Got last known location. In some rare situations this can be null.
//                if (location != null) {
//                    val coord = location.latitude.plus(location.longitude)
//                    Log.e("coordinates:", coord.toString())
//
//                }
//            }
//    }

//    /** Sends an email to each contact in the user's contact list **/
//    private fun sendEmail() {
//        val sharedPref =
//            this.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
//        val defaultValue = getString(R.string.current_user_id )
//        val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)
//        if (userId != null) {
//            // Get list from firebase based on userID
//            getContacts(userId) {
//                for (item in it) {
//                    // Iterate through and send text messages
//                    val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts(
//                        "mailto", item.contactEmail, null))
//                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, resources.getString(R.string.default_email_subject))
//                    emailIntent.putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.default_text_message))
//                    startActivity(Intent.createChooser(emailIntent, resources.getString(R.string.label_send_email)))
//                }
//            }
//        }
//
//    }
}