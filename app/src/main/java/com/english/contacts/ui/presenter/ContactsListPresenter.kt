package com.english.contacts.ui.presenter

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler
import com.english.contacts.R
import com.english.contacts.data.IndexHeader
import com.english.contacts.data.converter.ContactsHeaderer
import com.english.contacts.data.model.Contact
import com.english.contacts.data.model.HeaderedModel
import com.english.contacts.data.repository.ContactsRepository
import com.english.contacts.ui.contacts.StickyHeaderContent
import com.english.contacts.ui.list.PagedListBuilderHelper
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ContactsListPresenter constructor(private val view: ContactsListContract.View?) :
    ContactsListContract.Presenter, StickyHeaderHandler, CoroutineScope {

    private val MAX_COUNT_FOR_INDEX_BAR: Int = 100
    private val ITEMS_PER_PAGE: Int = 100

    private var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() {
            if (job == null) {
                job = Job()
            }
            return Dispatchers.Main + job!!
        }

    private var contacts: LiveData<PagedList<HeaderedModel<Contact>>>
    private val config =
        PagedList.Config.Builder().setPageSize(ITEMS_PER_PAGE).setEnablePlaceholders(false).build()
    val contactsCount: Int get() = contacts.value?.loadedCount ?: 0

    private var lastLoadedContact: Contact? = null

    private val stickyHeaders: MutableList<StickyHeaderContent?> = ArrayList()
    private var indexBarHeaders: MutableList<IndexHeader> = ArrayList()
    private val headerToPositionMapping: MutableMap<String, Int> = HashMap()
    private var shouldShowIndexBar = false

    lateinit var contactsRepository: ContactsRepository

    init {
        contactsRepository = ContactsRepository(ContactsHeaderer())
        contacts = initializedPagedListBuilder(config).build()
    }

    private fun initializedPagedListBuilder(config: PagedList.Config)
            : LivePagedListBuilder<Int, HeaderedModel<Contact>> {
        return LivePagedListBuilder(PagedListBuilderHelper<HeaderedModel<Contact>>().apply {
            loadInitial = { _, callback ->
                if (contactsCount != 0) {
                    runBlocking(Dispatchers.Default) { loadInitialData(callback, this) }
                } else {
                    launch(Dispatchers.Default) { loadInitialData(callback, this) }
                }
            }
            loadAfter = { params, callback ->
                runBlocking(Dispatchers.Default) { loadAfterData(params, callback, this) }
            }
        }.getDataSourceFactory(), config)
    }

    private suspend fun loadInitialData(
        callback: PageKeyedDataSource.LoadInitialCallback<Int, HeaderedModel<Contact>>,
        scope: CoroutineScope
    ) {
        showProgress(true)
        val headeredContactsResponse =
            contactsRepository.getContacts(1, ITEMS_PER_PAGE, null, scope)

        stickyHeaders.clear()
        headerToPositionMapping.clear()
        indexBarHeaders = ArrayList()
        for ((position, headeredContact) in headeredContactsResponse.headeredContacts.withIndex()) {
            if (headeredContact.type == HeaderedModel.TYPE_STICKY_HEADER) {
                stickyHeaders.add(StickyHeaderContent(headeredContact.header!!))
                indexBarHeaders.add(IndexHeader(headeredContact.header!!, headeredContact.header!!))
                headerToPositionMapping[headeredContact.header!!] = position
            } else {
                stickyHeaders.add(null)
                lastLoadedContact = headeredContact.model
            }
        }

        callback.onResult(
            headeredContactsResponse.headeredContacts, 1,
            PagedListBuilderHelper.getNextPage(1, headeredContactsResponse.apiCount, ITEMS_PER_PAGE)
        )

        shouldShowIndexBar = headeredContactsResponse.apiCount < MAX_COUNT_FOR_INDEX_BAR

        withContext(Dispatchers.Main) {
            updateIndexBarView()
            hideProgress(headeredContactsResponse.headeredContacts.isEmpty())
            view?.resetListPosition()
        }
    }

    private suspend fun loadAfterData(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, HeaderedModel<Contact>>,
        scope: CoroutineScope
    ) {
        showProgress(false)
        val headeredClientResponse = contactsRepository
            .getContacts(params.key, ITEMS_PER_PAGE, lastLoadedContact, scope)

        for (headeredClient in headeredClientResponse.headeredContacts) {
            if (headeredClient.type == HeaderedModel.TYPE_STICKY_HEADER) {
                stickyHeaders.add(StickyHeaderContent(headeredClient.header!!))
            } else {
                stickyHeaders.add(null)
                lastLoadedContact = headeredClient.model
            }
        }

        callback.onResult(
            headeredClientResponse.headeredContacts,
            PagedListBuilderHelper.getNextPage(
                params.key,
                headeredClientResponse.apiCount,
                ITEMS_PER_PAGE
            )
        )

        withContext(Dispatchers.Main) {
            hideProgress(headeredClientResponse.headeredContacts.isEmpty())
        }
    }

    override fun getAdapterData(): MutableList<*> {
        return stickyHeaders
    }

    private fun showProgress(initial: Boolean) {
        launch {
            if (initial) {
                view?.setRefreshIndicator(true)
            }
        }
    }

    private fun hideProgress(isEmptyList: Boolean) {
        view?.apply {
            setLoadingIndicator(false)
            setRefreshIndicator(false)
        }
    }

    override fun start() {
        if (contacts.value == null || contactsCount == 0) {
            onRefresh()
        }
    }

    override fun onStop() {
        job?.cancel()
        job = null
    }

    override fun onRefresh() {
        invalidateList()
    }

    override fun invalidateList() {
        contacts.value?.dataSource?.invalidate()
    }

    override fun onNetworkError() {
        view?.setRefreshIndicator(false)
        view?.showErrorMessage(R.string.error_no_network)
    }

    override fun onItemDetailRequested(item: HeaderedModel<Contact>?, position: Int) {
        view?.showItemDetail(item, position)
    }

    override fun getContacts() = contacts

    override fun updateListState(updatedItemPosition: Int?) {
        //used for updating item inside list by it position
    }

    override fun onIndexBarViewHeaderClicked(header: String?) {
        if (headerToPositionMapping.containsKey(header)) {
            headerToPositionMapping[header]?.let { position ->
                view?.setItemToTop(position)
            }
        }
    }

    override fun updateIndexBarView() {
        if (shouldShowIndexBar) {
            view?.showListNavigationLetters(indexBarHeaders)
        }
    }
}