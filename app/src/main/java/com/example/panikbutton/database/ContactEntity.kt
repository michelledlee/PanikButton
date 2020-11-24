package com.example.panikbutton.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class ContactEntity (
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "contactName") val contactName: String?,
    @ColumnInfo(name = "contactPhone") val contactPhone: Long?,
    @ColumnInfo(name = "contactEmail") val contactEmail: String?,
) {
    constructor(contactName: String?, contactPhone: Long?, contactEmail: String?): this(Int.MIN_VALUE, contactName, contactPhone, contactEmail)
}

