package com.example.panikbutton.ui.profile.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.panikbutton.R
import kotlinx.android.synthetic.main.fragment_profile.view.*
import java.util.*

class ProfileFragment : Fragment() {
    private val US_PHONE_LENGTH = 10
    private lateinit var primaryNameView: View
    private lateinit var primaryPhoneView: View
    private lateinit var primaryEmailView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_profile, container, false)

        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        // Set up user profile details
        val defaultUserNameValue = resources.getString(R.string.user_name)
        val defaultUserPhoneValue = resources.getString(R.string.user_phone)
        val defaultUserEmailValue = resources.getString(R.string.user_email)
        val currentUserName = sharedPref?.getString(getString(R.string.current_user_name), defaultUserNameValue)
        val currentPhone = sharedPref?.getString(getString(R.string.current_phone_name), defaultUserPhoneValue)
        val currentEmail = sharedPref?.getString(getString(R.string.current_email_name), defaultUserEmailValue)

        view.profile_userName.text = currentUserName
        view.profile_userPhone.text = formatPhone(currentPhone.toString())
        Log.e("number - lollipop", currentPhone.toString())
        Log.e("country", Locale.getDefault().country)
        view.profile_userEmail.text = currentEmail

        // Set up primary contact information
//        primaryNameView = view.profile_primaryName
//        primaryPhoneView = view.profile_primaryPhone
//        primaryEmailView = view.profile_primaryEmail
//        loadPrimaryContact()

        view.profile_editButton.setOnClickListener {
            val intent = Intent (activity, EditProfileActivity::class.java)
            activity?.startActivity(intent)
        }

        return view
    }

    /** Format phone number for display purposes **/
    private fun formatPhone(number: String): String {
        if (number.length == US_PHONE_LENGTH) {
            val areaCode = number.substring(0, 3)
            val prefix = number.substring(3, 6)
            val subscriber = number.substring(6)
            return "($areaCode) $prefix-$subscriber"
        }
        return number
    }

    /** Refresh primary contact info **/
    private fun loadPrimaryContact() {
        val sharedPref = activity?.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE)

        val primaryContactNameValue = resources.getString(R.string.primary_contact_name)
        val primaryContactPhoneValue = resources.getString(R.string.primary_contact_number)
        val primaryContactEmailValue = resources.getString(R.string.primary_contact_email)

        val primaryContactName = sharedPref?.getString(getString(R.string.primary_contact_name), primaryContactNameValue)
        val primaryPhone = sharedPref?.getString(getString(R.string.primary_contact_number), primaryContactPhoneValue)
        val primaryEmail = sharedPref?.getString(getString(R.string.primary_contact_email), primaryContactEmailValue)
        (primaryNameView as TextView?)?.text = primaryContactName
        (primaryPhoneView as TextView?)?.text = primaryPhone
        (primaryEmailView as TextView?)?.text = primaryEmail
    }
}