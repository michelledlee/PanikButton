package com.example.panikbutton.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.panikbutton.R
import com.example.panikbutton.database.ContactEntity
import com.example.panikbutton.ui.profile.contacts.ContactsAdapter
import com.example.panikbutton.util.TextItemViewHolder

class ContactAdapter : RecyclerView.Adapter<TextItemViewHolder>() {
    var data = listOf<ContactEntity>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.text_item_view, parent, false) as TextView
        return TextItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: TextItemViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.contactName
    }

    override fun getItemCount() = data.size
}