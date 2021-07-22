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
import android.view.*
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.panikbutton.R
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.example.panikbutton.ui.about.AboutActivity
import com.example.panikbutton.ui.profile.ProfileActivity
import com.example.panikbutton.ui.profile.user.EditProfileActivity
import com.example.panikbutton.ui.settings.SettingsActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.database.DatabaseReference
import java.util.jar.Manifest

const val SMS_CODE = 1
const val GPS_CODE = 2


class HomeActivity : AppCompatActivity(), ReadAndWriteSnippets {
    override lateinit var database: DatabaseReference
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_home)

        initializeDbRef()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
        val panikButton: View = findViewById(R.id.panic_button)
        panikButton.setOnClickListener {
            checkSMSPermission(SMS_CODE)
            checkGPSPermission(GPS_CODE)
//            sendSMS()
        }
    }

    /** Create options menu **/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        return true
    }

    /** Setting up menu options **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_profile -> {
                launchProfile()
                true
            }
            R.id.action_settings -> {
                launchSettings()
                true
            }
            R.id.action_about -> {
                launchAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Brings user to the Profile screen via menu selection  **/
    private fun launchProfile() {
        val intent = Intent (this, ProfileActivity::class.java)
        startActivity(intent)
    }

    /** Brings user to the Settings screen via menu selection **/
    private fun launchSettings() {
        val intent = Intent (this, SettingsActivity::class.java)
        startActivity(intent)
    }

    /** Brings user to the About screen via menu selection **/
    private fun launchAbout() {
        val intent = Intent (this, AboutActivity::class.java)
        startActivity(intent)
    }

    /** Check permissions for SMS functionality **/
    private fun checkSMSPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.SEND_SMS), requestCode)
        } else {
            Toast.makeText(this, "SMS already granted", Toast.LENGTH_SHORT).show()
        }

    }

    /** Check permissions for SMS functionality **/
    private fun checkGPSPermission(requestCode: Int) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), requestCode)
        } else {
            Toast.makeText(this, "GPS already granted", Toast.LENGTH_SHORT).show()
            sendSMS()
        }

    }

    /** Displays dialog explaining why the SMS permissions is needed **/
    private fun showInContextUISMS() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.sms_permissions_layout)
        dialog.show()
        val okayButton = dialog.findViewById<Button>(R.id.okay_sms_perm)
        okayButton.setOnClickListener() {
            dialog.dismiss()
        }
    }

    /** Displays dialog explaining why the SMS permissions is needed **/
    private fun showInContextUILocation() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.background_location_layout)
        dialog.show()
        val okayButton = dialog.findViewById<Button>(R.id.okay_bg_perm)
        okayButton.setOnClickListener() {
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
                        Log.e("hi", smsMessage)
                        Toast.makeText(this, verificationMsg, Toast.LENGTH_SHORT).show()
                    } else {
                        val message : String = String.format(getString(R.string.invalid_phone_number), contactName)
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }

                }
            }
        }
    }

    /** Method that is called on receipt of permissions request to user **/
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            1 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action.
                    sendSMS()
                    Log.e("sms", "sent sms")
                } else {
                    // Explain to the user that the feature is unavailable
                    showInContextUISMS()
                    Log.e("sms", "explain sms")
                }
                return
            }
            2 -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission is granted. Continue the action
                    Log.e("gps", "sent gps")
                } else {
                    // Explain to the user that the feature is unavailable
                    showInContextUILocation()
                    Log.e("gps", "explain gps")
                }
                return
            }
            else -> {
                // Ignore all other requests.
            }
        }
    }

}