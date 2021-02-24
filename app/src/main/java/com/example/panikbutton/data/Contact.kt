package com.example.panikbutton.data

import kotlinx.serialization.Serializable

//@Serializable
//class Contact(
//    val id: Int,
//    val contactName: String,
//    val contactPhone: Long,
//    val contactEmail: String
//)

data class Contact(
    val id: Int,
    val contactName: String,
    val contactPhone: Long,
    val contactEmail: String
) {
    constructor() : this(-1, "", -1, "")
}

//fun Contact(contact: Contact) : Contact{
//    return contact
//}