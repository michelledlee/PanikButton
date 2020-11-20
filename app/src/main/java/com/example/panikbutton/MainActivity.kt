package com.example.panikbutton

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.panikbutton.ui.about.AboutActivity
import com.example.panikbutton.ui.profile.mainProfile.ProfileActivity
import com.example.panikbutton.ui.profile.mainProfile.ProfileDialogFragment
import com.example.panikbutton.ui.settings.SettingsActivity

class MainActivity : AppCompatActivity(), ProfileDialogFragment.ProfileDialogListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        val sharedPref = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        val initialStart = sharedPref.getString(getString(R.string.entered_user_info), "true")
        if (initialStart == "true") {
            showProfileDialog()
        }
    }

    /* Function to show the profile dialog */
    private fun showProfileDialog() {
        val dialog = ProfileDialogFragment()
        dialog.isCancelable = false
        dialog.show(supportFragmentManager, "ProfileDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }

    override fun onDialogNegativeClick(dialog: DialogFragment) {
        TODO("Not yet implemented")
    }
}