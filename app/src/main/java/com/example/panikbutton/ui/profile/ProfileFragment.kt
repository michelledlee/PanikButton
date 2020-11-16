package com.example.panikbutton.ui.profile//package com.example.panikbutton.ui.todelete

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.panikbutton.R
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val defaultValue = null
        val userName = sharedPref?.getString(getString(R.string.user_name), defaultValue)
        val userPhone = sharedPref?.getString(getString(R.string.user_phone), defaultValue)
        val userEmail = sharedPref?.getString(getString(R.string.user_email), defaultValue)

        userName?.let {
            profile_userName.text = context?.getString(R.string.updated_name, userName)
        }
        userPhone?.let {
            profile_userPhone.text = context?.getString(R.string.updated_phone, userPhone)
        }
        userEmail?.let {
            profile_userEmail.text = context?.getString(R.string.updated_email, userEmail)
        }

//        edit_user_profile_button.setOnClickListener {
//        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }
}