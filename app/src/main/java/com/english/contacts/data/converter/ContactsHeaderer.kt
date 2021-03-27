package com.english.contacts.data.converter

import com.english.contacts.data.model.Contact
import com.english.contacts.data.model.HeaderedModel

class ContactsHeaderer {

    public fun headeringContacts(Contacts: List<Contact>,
                                  lastItem: Contact?): List<HeaderedModel<Contact>> {
        val ContactWithHeaderList: MutableList<HeaderedModel<Contact>> = mutableListOf()

        lastItem?.let {
            if (it.title.isNotEmpty() && Contacts.isNotEmpty() &&
                Contacts[0].title.isNotEmpty() &&
                it.title[0].toUpperCase() != Contacts[0].title[0].toUpperCase()) {
                ContactWithHeaderList.add(
                    HeaderedModel<Contact>(null,
                    Contacts[0].title[0].toString(),
                    HeaderedModel.TYPE_STICKY_HEADER)
                )
            }
        }

        if (lastItem == null) {
            if (Contacts.isNotEmpty() && Contacts[0].title.isNotEmpty()) {
                ContactWithHeaderList.add(
                    HeaderedModel<Contact>(null,
                        Contacts[0].title[0].toString(),
                        HeaderedModel.TYPE_STICKY_HEADER)
                )
            }
        }

        for (i in Contacts.indices) {
            if (i > 0 && Contacts[i].title.isNotEmpty() &&
                Contacts[i - 1].title.isNotEmpty() &&
                Contacts[i].title[0].toUpperCase() !=
                Contacts[i - 1].title[0].toUpperCase()) {
                ContactWithHeaderList.add(
                    HeaderedModel<Contact>(null,
                        Contacts[i].title[0].toString(),
                        HeaderedModel.TYPE_STICKY_HEADER)
                )
            }
            ContactWithHeaderList.add(
                HeaderedModel<Contact>(
                Contacts[i], null, HeaderedModel.TYPE_MODEL)
            )
        }

        return ContactWithHeaderList
    }
}