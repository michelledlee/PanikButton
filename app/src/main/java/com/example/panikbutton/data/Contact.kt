package com.example.panikbutton.data

import kotlinx.serialization.Serializable

@Serializable
class Contact(
    val id: Int,
    val contactName: String,
    val contactPhone: Long,
    val contactEmail: String
)

fun Contact(contact: Contact) : Contact{
    return contact
}