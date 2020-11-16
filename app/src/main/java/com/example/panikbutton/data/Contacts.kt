package com.example.panikbutton.data

import android.content.res.Resources

/* Returns initial list of contacts. */
fun contactList(resources: Resources): List<Contact> {
    return listOf(
        Contact(
            id = 1,
            contactName = "Contact1",
            contactPhone = 1112223333,
            contactEmail = "Contact1@email.com",
        ),
        Contact(
            id = 2,
            contactName = "Contact2",
            contactPhone = 222333444,
            contactEmail = "Contact2@email.com",
        )
    )
}