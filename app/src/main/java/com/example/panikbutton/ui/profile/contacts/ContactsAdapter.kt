package com.example.panikbutton.ui.profile.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.panikbutton.R
import com.example.panikbutton.data.Contact

class ContactsAdapter(private val onClick: (Contact) -> Unit) :
    ListAdapter<Contact, ContactsAdapter.ContactViewHolder>(ContactDiffCallback) {

    /* ViewHolder for Contact, takes in the inflated view and the onClick behavior. */
    class ContactViewHolder(itemView: View, val onClick: (Contact) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val contactNameTextView: TextView = itemView.findViewById(R.id.contact_name)
//        private val contactImageView: ImageView = itemView.findViewById(R.id.contact_image)
        private val contactPhoneTextView: TextView = itemView.findViewById(R.id.contact_phone)
        private val contactEmailTextView: TextView = itemView.findViewById(R.id.contact_email)
        private var currentContact: Contact? = null

        init {
            itemView.setOnClickListener {
                currentContact?.let {
                    onClick(it)
                }
            }
        }

        /* Bind contact info. */
        fun bind(contact: Contact) {
            currentContact = contact

            contactNameTextView.text = contact.contactName
            // EXTRA: For if we want to set avatar images later
//            if (contact.image != null) {
//                contactImageView.setImageResource(contact.image)
//            } else {
//                contactImageView.setImageResource(R.drawable.avatar)
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = getItem(position)
        holder.bind(contact)
    }
}

object ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.contactPhone == newItem.contactPhone
    }
}