package com.example.panikbutton.data

/* Returns initial list of contacts. */
fun contactList(): List<Contact> {
    return listOf(
        Contact(
            id = 1,
            contactName = "Contact1",
            contactPhone = 9055097899,
            contactEmail = "Contact1@email.com",
        ),
        Contact(
            id = 2,
            contactName = "Contact2",
            contactPhone = 8185939830,
            contactEmail = "Contact2@email.com",
        )
    )
}