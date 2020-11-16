package com.example.panikbutton.ui.profile.contacts//package com.example.panikbutton.ui.todelete

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.panikbutton.R
import com.example.panikbutton.data.Contact
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView

const val CONTACT_ID = "contact id"

class ContactsFragment : Fragment() {
    private val newContactActivityRequestCode = 1
    private val contactsListViewModel by viewModels<ContactsViewModel> {
        ContactsListViewModelFactory(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_contacts, container, false)

        /* Instantiates headerAdapter and contactsAdapter. Both adapters are added to concatAdapter.
        which displays the contents sequentially */
        val headerAdapter = HeaderAdapter()
        val contactsAdapter = ContactsAdapter { contact -> adapterOnClick(contact) }
        val concatAdapter = ConcatAdapter(headerAdapter, contactsAdapter)

        val recyclerView: RecyclerView = view.findViewById(R.id.contacts_recyclerView)
        recyclerView.adapter = concatAdapter

        contactsListViewModel.contactLiveData.observe(viewLifecycleOwner, {
            it?.let {
                contactsAdapter.submitList(it as MutableList<Contact>)
                headerAdapter.updateContactCount(it.size)
            }
        })

        return view
    }

    /* Opens ContactDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(contact: Contact) {
        val intent = Intent(requireContext(), ContactDetailActivity()::class.java)
        intent.putExtra(CONTACT_ID, contact.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        /* Inserts contact into viewModel. */
        if (requestCode == newContactActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val contactName = data.getStringExtra(CONTACT_NAME)
                val contactPhone = data.getIntExtra(CONTACT_PHONE, -1)
                val contactEmail = data.getStringExtra(CONTACT_EMAIL)

                contactsListViewModel.insertContact(contactName, contactPhone, contactEmail)
            }
        }
    }
}
