package com.example.panikbutton.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactDao {
    @Query("SELECT * FROM contact")
    fun getAll(): LiveData<List<ContactEntity>>

    @Query("SELECT COUNT(*) FROM contact")
    suspend fun getSize(): Integer

    @Query("SELECT * FROM contact WHERE id IN (:contactIds)")
    suspend fun loadAllByIds(contactIds: IntArray): List<ContactEntity>

    @Query("SELECT * FROM contact WHERE contactName LIKE :name LIMIT 1")
    suspend fun findByName(name: String): ContactEntity

    @Insert
    suspend fun insert(contact: ContactEntity)

    @Insert
    suspend fun insertAll(vararg contacts: ContactEntity)

    @Delete
    suspend fun delete(contact: ContactEntity)
}