package com.example.panikbutton.ui.profile.mainProfile

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

        //TODO("Open shared preferences, if it is not null, update the hints, if it is, default")
        findViewById<Button>(R.id.save_user_profile).setOnClickListener {
            TODO("Save to shared preferences")
        }
    }
}