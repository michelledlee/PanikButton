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

        val defaultUserNameValue = resources.getString(R.string.user_name)
        val defaultUserPhoneValue = resources.getString(R.string.user_name)
        val defaultUserEmailValue = resources.getString(R.string.user_name)
        val currentUserName = sharedPref?.getString(getString(R.string.current_user_name), defaultUserNameValue)
        val currentPhoneName = sharedPref?.getString(getString(R.string.current_phone_name), defaultUserPhoneValue)
        val currentEmailName = sharedPref?.getString(getString(R.string.current_email_name), defaultUserEmailValue)

        view.profile_userName.text = currentUserName
        view.profile_userPhone.text = currentPhoneName
        view.profile_userEmail.text = currentEmailName

        view.profile_editButton.setOnClickListener {
            val intent = Intent (activity, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        return view
    }
}