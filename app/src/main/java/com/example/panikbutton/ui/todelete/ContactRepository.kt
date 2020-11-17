package com.example.panikbutton.ui.todelete

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.example.panikbutton.R
import com.example.panikbutton.data.Contact
import com.example.panikbutton.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types


class ContactRepository(val app: Application) {

    val contactData = MutableLiveData<List<Contact>>()

    private val listType = Types.newParameterizedType(
        List::class.java, Contact::class.java
    )

    init {
        getContactData()
    }

    fun getContactData() {
        val text = FileHelper.getTextFromResources(app, R.raw.contact_data)
        val moshi = Moshi.Builder().build()
        val adapter: JsonAdapter<List<Contact>> = moshi.adapter(listType)
        contactData.value = adapter.fromJson(text) ?: emptyList()
    }
}