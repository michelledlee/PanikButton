package com.example.panikbutton.toDelete//package com.example.panikbutton.ui.profile.contacts
//
//import android.content.Context
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import com.example.panikbutton.data.Contact
//import com.example.panikbutton.data.DataSource
//import kotlin.random.Random
//
//class ContactsViewModel(val dataSource: DataSource) : ViewModel() {
//
//    val contactLiveData = dataSource.getContactList()
//
//    /* If the name and email or phone are present, create new Contact and add it to the datasource */
//    fun insertContact(contactName: String?, contactPhone: Long, contactEmail: String?) {
//        if (contactName == null) {
//            return
//        }
//
////        // EXTRA: Potentially used for avatars in the future
////        val image = dataSource.getRandomContactImageAsset()
//        val newContact = Contact(
//            Random.nextInt(),
//            contactName,
//            contactPhone,
//            contactEmail.toString()
//        )
//
//        dataSource.addContact(newContact)
//    }
//}
//
//class ContactsListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
//    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//        @Suppress("UNCHECKED_CAST")
//        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
//            return ContactsViewModel(
//                dataSource = DataSource.getDataSource()
//            ) as T
//        }
//        throw IllegalArgumentException("Unknown ViewModel class")
//    }
//
//}
