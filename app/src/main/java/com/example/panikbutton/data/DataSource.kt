package com.example.panikbutton.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

//class DataSource(resources: Resources, sharedPref : SharedPreferences): ViewModel() {
class DataSource: ViewModel() {

    private val initialContactList = contactList()
//    private val initialContactList = getInitialContactList(resources, sharedPref)
    private val contactsLiveData = MutableLiveData(initialContactList)

    /* Adds contact to liveData and posts value. */
    fun addContact(contact: Contact) {
        val currentList = contactsLiveData.value
        if (currentList == null) {
            contactsLiveData.postValue(listOf(contact))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, contact)
            contactsLiveData.postValue(updatedList)
        }
    }

    /* Removes contact from liveData and posts value. */
    fun removeContact(contact: Contact) {
        val currentList = contactsLiveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(contact)
            contactsLiveData.postValue(updatedList)
        }
    }

    /* Returns contact given an ID. */
    fun getContactForId(id: Int): Contact? {
        contactsLiveData.value?.let { contacts ->
            return contacts.firstOrNull{ it.id == id}
        }
        return null
    }

    fun getContactList(): LiveData<List<Contact>> {
        return contactsLiveData
    }

//    fun getInitialContactList(resources : Resources, sharedPref: SharedPreferences) : List<Contact> {
//        // Get the file name
//        val uri = sharedPref.getString(resources.getString(R.string.contactsURI), "false")
//            ?.replace("file:/","")
////        Log.e("initial URI:", uri.toString())
////        File(uri).canWrite()
////        FileWriter(uri).append("testing")
////        FileWriter(uri).flush()
////        val fileContents : String = File(uri).readText()
////        // Testing the file output
////        Log.e("File contents", fileContents.length.toString())
//        val testfileName = "Contacts.txt"
//        FileWriter(testfileName).write("hello world")
//        FileWriter(testfileName).flush()
//        Log.e("testFileLength", testfileName.length.toString())
//        val contactList : ArrayList<Contact> = arrayListOf()
//        if (uri != "false") {
////            val parsedURI = URI.create(uri)
////            Log.e("parsedURI:", parsedURI.toString())
//            File(uri).forEachLine {
//                Log.e("line:", it)
//                it.split(".")
//                contactList.add(Contact(it[0].toLong(), it[1].toString(), it[2].toLong(), it[3].toString()))
//            }
//        }
//        return contactList
//    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource()
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}