package com.example.panikbutton.data

import kotlinx.serialization.Serializable

@Serializable
class Contact(
//data class Contact (
    val id: Long,
    val contactName: String,
    val contactPhone: Long,
    val contactEmail: String
)