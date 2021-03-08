package com.example.panikbutton.data

import com.google.firebase.database.Exclude
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

    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "id" to id,
            "contactName" to contactName,
            "contactPhone" to contactPhone,
            "contactEmail" to contactEmail
        )
    }
}

//fun Contact(contact: Contact) : Contact{
//    return contact
//}