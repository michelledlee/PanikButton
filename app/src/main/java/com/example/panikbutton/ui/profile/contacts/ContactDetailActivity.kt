package com.example.panikbutton.ui.profile.contacts

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.panikbutton.R

class ContactDetailActivity : AppCompatActivity() {

    private val contactDetailViewModel by viewModels<ContactDetailViewModel> {
        ContactDetailViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contact_item)

        var currentContactId: Long? = null

        /* Connect variables to UI elements. */
        val contactName: TextView = findViewById(R.id.contact_name)
//        val contactImage: ImageView = findViewById(R.id.flower_detail_image)
        val contactPhone: TextView = findViewById(R.id.contact_phone)
        val contactEmail: TextView = findViewById(R.id.contact_email)
        val editContactButton: Button = findViewById(R.id.edit_contact_button)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentContactId = bundle.getLong(CONTACT_ID)
        }

        /* If currentContactId is not null, get corresponding contact details */
        currentContactId?.let {
            val currentContact = contactDetailViewModel.getContactForId(it)
            contactName.text = currentContact?.contactName
//            if (currentContact?.image == null) {
//                flowerImage.setImageResource(R.drawable.rose)
//            } else {
//                flowerImage.setImageResource(currentFlower.image)
//            }
            contactPhone.text = currentContact?.contactPhone.toString()
            contactEmail.text = currentContact?.contactEmail

            editContactButton.setOnClickListener {
                if (currentContact != null) {
                    contactDetailViewModel.removeContact(currentContact)
                }
                finish()
            }
        }

    }
}