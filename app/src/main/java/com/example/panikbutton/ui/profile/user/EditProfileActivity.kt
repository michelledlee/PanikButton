package com.example.panikbutton.ui.profile.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import com.example.panikbutton.R
import com.example.panikbutton.data.ReadAndWriteSnippets
import com.example.panikbutton.ui.profile.ProfileActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.edit_profile_layout.*

class EditProfileActivity : AppCompatActivity(), ReadAndWriteSnippets {
    private lateinit var editUserName: TextInputEditText
    private lateinit var editUserPhone: TextInputEditText
    private lateinit var editUserEmail: TextInputEditText

    override lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_profile_layout)

        editUserName = findViewById(R.id.edit_user_name)
        editUserPhone = findViewById(R.id.edit_user_phone)
        editUserEmail = findViewById(R.id.edit_user_email)

        // Getting default user values from SharedPreferences
        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultUserNameValue = resources.getString(R.string.user_name)
        val defaultUserPhoneValue = resources.getString(R.string.user_phone)
        val defaultUserEmailValue = resources.getString(R.string.user_email)
        val currentUserName = sharedPref.getString(getString(R.string.current_user_name), defaultUserNameValue)
        val currentPhoneName = sharedPref.getString(getString(R.string.current_phone_name), defaultUserPhoneValue)
        val currentEmailName = sharedPref.getString(getString(R.string.current_email_name), defaultUserEmailValue)

        editUserName.setText(currentUserName)
        editUserPhone.setText(currentPhoneName)
        editUserEmail.setText(currentEmailName)

        // Updating SharedPreferences with new user data
        findViewById<Button>(R.id.save_user_profile).setOnClickListener {
            if (saveUserData()) {
                // Go back to the Profile activity
                val intent = Intent (this, ProfileActivity::class.java)
                startActivity(intent)
            }
        }

        // Initialize Firebase
        database = Firebase.database.reference

    }

    /** Save updated user data to Shared Preferences and Firebase **/
    private fun saveUserData(): Boolean {
        val inputtedPhone = findViewById<TextInputEditText>(R.id.edit_user_phone).text.toString()
        val inputtedEmail = findViewById<TextInputEditText>(R.id.edit_user_email).text.toString()
        val inputtedName = findViewById<TextInputEditText>(R.id.edit_user_name).text.toString()

        /* Form validation */
        // Validation for phone number
        if (!validatePhone(inputtedPhone)) {
            return false
        }
        // Validation for email
        if (!inputtedEmail.isEmailValid()) {
            return false
        }

        // Save the user details to SharedPreferences
        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        // Save all updates to shared preferences
        sharedPref.edit().putString(getString(R.string.current_user_name), inputtedName).apply()
        sharedPref.edit().putString(getString(R.string.current_phone_name), inputtedPhone).apply()
        sharedPref.edit().putString(getString(R.string.current_email_name), inputtedEmail).apply()

        val defaultValue = resources.getString(R.string.current_user_id)
        val userId = sharedPref.getString(getString(R.string.current_user_id), defaultValue)

        // Update Firebase
        if (userId != null) {
            editUser(userId, inputtedName, inputtedPhone.toLong(), inputtedEmail)
        }

        return true
    }

    /** Validate phone number so it can be formatted correctly in UI **/
    private fun validatePhone(inputtedPhone: String): Boolean {
        var valid = true

        // Check if number is empty
        if (inputtedPhone.isEmpty()) {
            edit_user_phone.error = "Required."
            valid = false
        }

        // Check length of number
        if (inputtedPhone.length != 10) {
            // Prevent submission until user enters a proper phone number
            edit_user_phone.error = "Enter a valid phone number."
            valid = false
        }

        // Check if all digits
        if (!inputtedPhone.isDigitsOnly()) {
            edit_user_phone.error = "Enter valid phone (without () or -)."
            valid = false
        }

        return valid
    }

    /** Validate email so it can be formatted correctly in UI **/
    private fun String.isEmailValid(): Boolean {
        val valid = !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
        if (!valid) {
            edit_user_email.error = "Enter a valid email."
        }
        return valid
    }


}