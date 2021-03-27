package com.english.contacts.ui.presenter

import androidx.annotation.StringRes

interface BaseContract {

    interface BaseView<T> {
        fun setLoadingIndicator(loading: Boolean)

        fun showItemDetail(item: T, position: Int)

        fun setRefreshIndicator(enabled: Boolean)

        fun showErrorMessage(@StringRes stringRes: Int)
    }

    interface BasePresenter<T> {
        fun start()

        fun onItemDetailRequested(item: T?, position: Int)

        fun updateListState(updatedItemPosition: Int?)

        fun onStop()

        fun onRefresh()

        fun invalidateList()

        fun onNetworkError()
    }
}