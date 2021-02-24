package com.example.panikbutton

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings.Global.getString
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
//import com.example.panikbutton.data.Contacts
import com.example.panikbutton.ui.about.AboutActivity
import com.example.panikbutton.ui.profile.ProfileActivity
//import com.example.panikbutton.ui.profile.user.ProfileDialogFragment
import com.example.panikbutton.ui.settings.SettingsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
//    class MainActivity : AppCompatActivity(), ProfileDialogFragment.ProfileDialogListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

////         Shared preferences
//        sharedPreferences = applicationContext.getSharedPreferences(
//            getString(R.string.preference_file_key),
//            Context.MODE_PRIVATE
//        )
////
////        // Context
//        instance = this

        /* Set views */
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        checkInitialStart()
    }

    public override fun onStart() {
        super.onStart()

        val currentUSer = auth.currentUser
//        updateUI(currentUser)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_about -> {
                AboutActivity.start(this)
            }
            R.id.action_profile-> {
                ProfileActivity.start(this)
            }
            R.id.action_settings -> {
                SettingsActivity.start(this)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /* Opens a dialog for the user to enter their profile details */
    private fun checkInitialStart() {
        // Getting default user values from SharedPreferences
        val sharedPref = getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val initialStart = sharedPref.getString(getString(R.string.entered_user_info), "true")
//        if (initialStart == "true") {
//            showProfileDialog()
//        }
    }

//    /* Function to show the profile dialog */
//    private fun showProfileDialog() {
//        val dialog = ProfileDialogFragment()
//        dialog.isCancelable = false
//        dialog.show(supportFragmentManager, "ProfileDialogFragment")
//    }
//
//    override fun onDialogPositiveClick(dialog: DialogFragment) {
//        TODO("Not yet implemented")
//    }
//
//    override fun onDialogNegativeClick(dialog: DialogFragment) {
//        TODO("Not yet implemented")
//    }

//    companion object {
//        lateinit var sharedPreferences: SharedPreferences
//        lateinit var instance: MainActivity private set
//    }
//
//    object Strings {
//        fun get(@StringRes stringRes: Int, vararg formatArgs: Any = emptyArray()): String {
//            return instance.getString(stringRes, *formatArgs)
//        }
//    }

}