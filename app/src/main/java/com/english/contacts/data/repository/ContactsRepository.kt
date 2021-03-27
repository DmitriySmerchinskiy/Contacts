package com.english.contacts.data.repository

import com.english.contacts.data.converter.ContactsHeaderer
import com.english.contacts.data.model.Contact
import com.english.contacts.data.model.HeaderedModel
import kotlinx.coroutines.CoroutineScope

class ContactsRepository(val headerer: ContactsHeaderer) {

    fun getContacts(page: Int, perPage: Int, lastItem: Contact?, scope: CoroutineScope): HeaderedContactsResponse {
        val contacts = testList()
        return HeaderedContactsResponse(headerer.headeringContacts(contacts, lastItem), contacts.size)
    }

    fun testList() : List<Contact> {
        return mutableListOf<Contact>().apply {
            add(Contact("Antony", "somebody","333 222 111"))
            add(Contact("Antony", "somebody","333 222 111"))
            add(Contact("Antony", "somebody","333 222 111"))
            add(Contact("Ben", "somebody","333 222 111"))
            add(Contact("Ben", "somebody","333 222 111"))
            add(Contact("Ben", "somebody","333 222 111"))
            add(Contact("Ben", "somebody","333 222 111"))
            add(Contact("Ben", "somebody","333 222 111"))
            add(Contact("Ben", "somebody","333 222 111"))
            add(Contact("Den", "somebody","333 222 111"))
            add(Contact("Den", "somebody","333 222 111"))
            add(Contact("Den", "somebody","333 222 111"))
            add(Contact("Den", "somebody","333 222 111"))
            add(Contact("Den", "somebody","333 222 111"))
            add(Contact("Mike", "somebody","333 222 111"))
            add(Contact("Mike", "somebody","333 222 111"))
            add(Contact("Mike", "somebody","333 222 111"))
            add(Contact("Mike", "somebody","333 222 111"))
            add(Contact("Mike", "somebody","333 222 111"))
            add(Contact("Mike", "somebody","333 222 111"))
            add(Contact("Piter", "somebody","333 222 111"))
            add(Contact("Piter", "somebody","333 222 111"))
            add(Contact("Piter", "somebody","333 222 111"))
            add(Contact("Piter", "somebody","333 222 111"))
            add(Contact("Piter", "somebody","333 222 111"))
        }
    }

    class HeaderedContactsResponse(val headeredContacts: List<HeaderedModel<Contact>>, val apiCount: Int)
}