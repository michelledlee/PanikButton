package com.example.panikbutton.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R
import com.example.panikbutton.ui.about.AboutActivity
import com.example.panikbutton.ui.home.HomeActivity
import com.example.panikbutton.ui.profile.ProfileActivity


class SettingsActivity : AppCompatActivity() {

    private lateinit var bottomNav: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        bottomNav = findViewById(R.id.navigation_home)
        bottomNav.setOnClickListener {
//            onBackPressed()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


        //Connect ble button
        val connectButton: View = findViewById(com.example.panikbutton.R.id.connect_button)
        connectButton.setOnClickListener {

        }

    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    /** Create options menu **/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_settings, menu)
        return true
    }

    /** Setting up menu options **/
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.action_profile -> {
                launchProfile()
                true
            }
            R.id.action_home -> {
                launchHome()
                true
            }
            R.id.action_about -> {
                launchAbout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /** Brings user to the Profile screen via menu selection  **/
    private fun launchProfile() {
        val intent = Intent (this, ProfileActivity::class.java)
        startActivity(intent)
    }

    /** Brings user to the Home screen via menu selection **/
    private fun launchHome() {
        val intent = Intent (this, HomeActivity::class.java)
        startActivity(intent)
    }

    /** Brings user to the About screen via menu selection **/
    private fun launchAbout() {
        val intent = Intent (this, AboutActivity::class.java)
        startActivity(intent)
    }

//    companion object {
//        fun start(context: Context) {
//            val intent = Intent(context, SettingsActivity::class.java)
//            context.startActivity(intent)
//        }
//    }
}