package com.english.contacts.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.brandongogetap.stickyheaders.StickyLayoutManager
import com.brandongogetap.stickyheaders.exposed.StickyHeaderHandler
import com.english.contacts.R
import com.english.contacts.data.IndexHeader
import com.english.contacts.data.model.Contact
import com.english.contacts.data.model.HeaderedModel
import com.english.contacts.ui.contacts.ContactsListAdapter
import com.english.contacts.ui.presenter.ContactsListContract
import com.english.contacts.ui.presenter.ContactsListPresenter
import com.english.contacts.ui.view.indexbar.IndexBarView

class MainActivity : AppCompatActivity(), ContactsListContract.View,
    IndexBarView.OnLetterClickListener {

    @BindView(R.id.recycler_view)
    lateinit var recycler: RecyclerView

    @BindView(R.id.index_bar_view)
    lateinit var indexBarView: IndexBarView

    var presenter: ContactsListContract.Presenter? = null
    var adapter: ContactsListAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        init()
    }

    fun init() {
        ButterKnife.bind(this)
        recycler.setItemViewCacheSize(0)
        createPresenter()

        createAdapter()
        setupRecycler()
        observeClients()

        setupStickyHeaders()
        setupIndexBarView()
    }

    override fun onResume() {
        super.onResume()
        presenter?.start()
    }

    override fun onStop() {
        super.onStop()
        presenter?.onStop()
    }

    fun createPresenter() {
        presenter = ContactsListPresenter(this)
    }

    fun createAdapter() {
        adapter = ContactsListAdapter(this)

    }

    private fun setupRecycler() {
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
    }

    private fun setupIndexBarView() {
        indexBarView.setOnLetterClickListener(this)
        presenter?.updateIndexBarView()
    }

    private fun setupStickyHeaders() {
        recycler.layoutManager = StickyLayoutManager(this, presenter as StickyHeaderHandler)
    }

    private fun observeClients() {
        presenter?.getContacts()?.observe(this, { adapter?.submitList(it) })
    }

    override fun resetListPosition() {
        setItemToTop(0)
    }

    override fun showListNavigationLetters(indexHeaders: List<IndexHeader?>?) {
        indexBarView.visibility = View.VISIBLE
        indexBarView.setIndexHeaders(indexHeaders)
    }

    override fun onLetterClicked(letter: String?) {
        presenter?.onIndexBarViewHeaderClicked(letter)
    }

    override fun setItemToTop(position: Int) {
        if (position > -1) {
            (recycler.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(position, 0)
        }
    }

    override fun setLoadingIndicator(loading: Boolean) {
        // TODO("Not yet implemented")
    }

    override fun showItemDetail(item: HeaderedModel<Contact>?, position: Int) {
        // TODO("Not yet implemented")
    }

    override fun setRefreshIndicator(enabled: Boolean) {
        // TODO("Not yet implemented")
    }

    override fun showErrorMessage(stringRes: Int) {
        // TODO("Not yet implemented")
    }
}