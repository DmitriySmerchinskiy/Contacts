package com.english.contacts.ui.presenter

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.english.contacts.data.IndexHeader
import com.english.contacts.data.model.Contact
import com.english.contacts.data.model.HeaderedModel

interface ContactsListContract {

    interface View : BaseContract.BaseView<HeaderedModel<Contact>?> {
        fun resetListPosition()
        fun showListNavigationLetters(indexHeaders: List<IndexHeader?>?)
        fun setItemToTop(position: Int)
    }

    interface Presenter : BaseContract.BasePresenter<HeaderedModel<Contact>?> {

        fun getContacts(): LiveData<PagedList<HeaderedModel<Contact>>>
        fun onIndexBarViewHeaderClicked(header: String?)
        fun updateIndexBarView()
    }
}