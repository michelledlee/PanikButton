package com.example.panikbutton.ui.profile.mainProfile//package com.example.panikbutton.ui.todelete

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.panikbutton.R
import com.example.panikbutton.ui.profile.contactDetail.EditContactActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*

class ProfileFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = "default value"
        val enteredUserInfo = sharedPref?.getBoolean(getString(R.string.entered_user_info), false)

        //TODO("Make the dialog to enter stuff")
        if (!enteredUserInfo!!) {

        }
        val userName = sharedPref?.getString(getString(R.string.user_name), defaultValue)
        val userPhone = sharedPref?.getString(getString(R.string.user_phone), defaultValue)
        val userEmail = sharedPref?.getString(getString(R.string.user_email), defaultValue)

        view.profile_userName.text = userName
        view.profile_userPhone.text = userPhone
        view.profile_userEmail.text = userEmail

        view.profile_editButton.setOnClickListener {
            val intent = Intent (activity, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        return view
    }
}