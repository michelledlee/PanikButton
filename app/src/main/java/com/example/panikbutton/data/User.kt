package com.example.panikbutton.data

import kotlinx.serialization.Serializable

@Serializable
class User (
    val userName: String,
    val userPhone: Long,
    val userEmail: String
)