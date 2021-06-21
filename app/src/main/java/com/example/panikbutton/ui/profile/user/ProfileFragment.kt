package com.example.panikbutton.ui.profile.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.example.panikbutton.R
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*

class ProfileFragment : Fragment() {
    private val US_PHONE_LENGTH = 10
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val defaultUserNameValue = resources.getString(R.string.user_name)
        val defaultUserPhoneValue = resources.getString(R.string.user_phone)
        val defaultUserEmailValue = resources.getString(R.string.user_email)
        val currentUserName = sharedPref?.getString(getString(R.string.current_user_name), defaultUserNameValue)
        val currentPhone = sharedPref?.getString(getString(R.string.current_phone_name), defaultUserPhoneValue)
        val currentEmail = sharedPref?.getString(getString(R.string.current_email_name), defaultUserEmailValue)

        view.profile_userName.text = currentUserName
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//            view.profile_userPhone.text = PhoneNumberUtils.formatNumber(
//                currentPhone.toString(),
//                Locale.getDefault().country
//            )
            view.profile_userPhone.text = formatPhone(currentPhone.toString())
            Log.e("number - lollipop", currentPhone.toString())
            Log.e("country", Locale.getDefault().country)
//        } else {
//            if (view.profile_userPhone.text.length == USPHONELENGTH) {
//                view.profile_userPhone.text = formatPhone(currentPhone.toString())
//                Log.e("number - custom", view.profile_userPhone.text.toString())
//            }
//        }

        view.profile_userEmail.text = currentEmail

        view.profile_editButton.setOnClickListener {
            val intent = Intent (activity, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        return view
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_main, menu)
    }

    private fun formatPhone(number: String): String {
        if (number.length == US_PHONE_LENGTH) {
            val areaCode = number.substring(0, 3)
            val prefix = number.substring(3, 6)
            val subscriber = number.substring(6)
            return "($areaCode) $prefix-$subscriber"
        }
        return number
    }
}