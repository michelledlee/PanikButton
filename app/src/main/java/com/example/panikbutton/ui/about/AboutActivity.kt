package com.example.panikbutton.ui.about

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R
import com.example.panikbutton.ui.home.HomeActivity
import com.example.panikbutton.ui.profile.ProfileActivity
import com.example.panikbutton.ui.settings.SettingsActivity

class AboutActivity : AppCompatActivity() {

    private lateinit var bottomNav: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        bottomNav = findViewById(R.id.navigation_home)
        bottomNav.setOnClickListener {
            onBackPressed()
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
            R.id.action_settings -> {
                launchSettings()
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

    /** Brings user to the Settings screen via menu selection **/
    private fun launchSettings() {
        val intent = Intent (this, SettingsActivity::class.java)
        startActivity(intent)
    }

//    companion object {
//        fun start(context: Context) {
//            val intent = Intent(context, AboutActivity::class.java)
//            context.startActivity(intent)
//        }
//    }
}