package com.example.panikbutton.ui.profile.mainProfile

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity : AppCompatActivity() {
    private lateinit var editUserName: TextInputEditText
    private lateinit var editUserPhone: TextInputEditText
    private lateinit var editUserEmail: TextInputEditText

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
        val defaultUserPhoneValue = resources.getString(R.string.user_name)
        val defaultUserEmailValue = resources.getString(R.string.user_name)
        val currentUserName = sharedPref.getString(getString(R.string.current_user_name), defaultUserNameValue)
        val currentPhoneName = sharedPref.getString(getString(R.string.current_phone_name), defaultUserPhoneValue)
        val currentEmailName = sharedPref.getString(getString(R.string.current_email_name), defaultUserEmailValue)

        editUserName.setText(currentUserName)
        editUserPhone.setText(currentPhoneName)
        editUserEmail.setText(currentEmailName)

        // Updating SharedPreferences with new user data
        findViewById<Button>(R.id.save_user_profile).setOnClickListener {
            saveNewUserData(sharedPref)
        }
    }

    private fun saveNewUserData(sharedPref : SharedPreferences, ) {
        with (sharedPref.edit()) {
            putString(getString(R.string.current_user_name), editUserName.text.toString())
            putString(getString(R.string.current_phone_name), editUserPhone.text.toString())
            putString(getString(R.string.current_email_name), editUserEmail.text.toString())
            apply()
        }
    }
}