package com.example.panikbutton.ui.profile.user

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.panikbutton.R
import com.google.android.material.textfield.TextInputEditText

class ProfileDialogFragment : DialogFragment() {
    /* This instance of the interface is used to deliver action events */
    internal lateinit var listener: ProfileDialogListener

    interface ProfileDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    /* Overridden to instantiate the ProfileDialogListener */
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the ProfileDialogListener so we can send events to the host
            listener = context as ProfileDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException((context.toString() +
                    " must implement ProfileDialogListener"))
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            // Get the layout inflater
            val inflater = requireActivity().layoutInflater

            val view = inflater.inflate(R.layout.profile_input_window, null)
            // Inflate and set the layout for the dialog
            // Pass null as the parent view because its going in the dialog layout
            builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.okay) { dialog, id ->
                    // Save the user details to SharedPreferences
                    val sharedPref = activity!!.getSharedPreferences(
                        getString(R.string.preference_file_key), Context.MODE_PRIVATE)

                    // If all fields have been entered, submit to SharedPreferences
                    val inputtedName = view.findViewById<TextInputEditText>(R.id.edit_profile_name).text.toString()
                    val inputtedPhone = view.findViewById<TextInputEditText>(R.id.edit_profile_phone).text.toString()
                    val inputtedEmail = view.findViewById<TextInputEditText>(R.id.edit_profile_email).text.toString()

                    if (inputtedName != "" && inputtedPhone != "" && inputtedEmail != "") {
                        sharedPref.edit().putString(getString(R.string.entered_user_info), "false").apply()
                        sharedPref.edit().putString(getString(R.string.current_user_name), inputtedName).apply()
                        sharedPref.edit().putString(getString(R.string.current_phone_name), inputtedPhone).apply()
                        sharedPref.edit().putString(getString(R.string.current_email_name), inputtedEmail).apply()
                    }
                }
                .setNegativeButton(R.string.later) { dialog, id ->
                    // No option to cancel
                }
                .setCancelable(false)
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}