package com.english.contacts.ui.contacts

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.english.contacts.R
import com.english.contacts.data.model.Contact
import com.english.contacts.data.model.HeaderedModel
import com.english.contacts.ui.list.BaseAdapter
import com.english.contacts.ui.list.VIEW_TYPE_ITEM

class ContactsListAdapter(val context: Context) : BaseAdapter<HeaderedModel<Contact>, RecyclerView.ViewHolder>
    (null, DIFF_CALLBACK) {

    init {
        initDiffer()
    }

    override fun initDiffer() {
        setDiffer(AsyncDifferConfig.Builder(DIFF_CALLBACK).build())
    }

    override fun onBindItemViewHolder(
        holder: RecyclerView.ViewHolder,
        item: HeaderedModel<Contact>?
    ) {
        if (holder is ContactViewHolder) {
            item?.model?.let { client ->
                holder.bind(client)
            }
        } else if (holder is ContactHeader) {
            holder.itemView.setOnClickListener(null)
            item?.header?.let { header ->
                holder.bind(header)
            }
        }
    }

    override fun getItemViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HeaderedModel.TYPE_STICKY_HEADER) {
            ContactHeader(
                LayoutInflater.from(context).inflate(
                    R.layout.item_sticky_header, parent, false
                )
            )
        } else {
            ContactViewHolder(
                LayoutInflater.from(context).inflate(
                    R.layout.item_layout, parent, false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        val viewType = super.getItemViewType(position)
        if (viewType == VIEW_TYPE_ITEM) {
            getItem(position)?.let { client ->
                return client.type
            }
        }
        return viewType
    }

    companion object {
        private val DIFF_CALLBACK: DiffUtil.ItemCallback<HeaderedModel<Contact>> = object :
            DiffUtil.ItemCallback<HeaderedModel<Contact>>() {

            override fun areItemsTheSame(
                oldItem: HeaderedModel<Contact>,
                newItem: HeaderedModel<Contact>
            ): Boolean {
                return if (oldItem.model != null && newItem.model != null) {
                    oldItem.model?.title == newItem.model?.title
                } else if (oldItem.header != null && newItem.header != null) {
                    oldItem.header == newItem.header
                } else false
            }

            override fun areContentsTheSame(
                oldItem: HeaderedModel<Contact>,
                newItem: HeaderedModel<Contact>
            ): Boolean {
                return if (oldItem.model != null && newItem.model != null) {
                    oldItem.model == newItem.model
                } else if (oldItem.header != null && newItem.header != null) {
                    oldItem.header == newItem.header
                } else false
            }
        }
    }
    
    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.title) lateinit var title: TextView
        @BindView(R.id.info) lateinit var info: TextView
        @BindView(R.id.phone) lateinit var phone: TextView

        init { ButterKnife.bind(this, itemView) }

        fun bind(contact: Contact?) {
            if (contact != null) {
                title.text = contact.title
                info.text = contact.info
                phone.text = contact.phone
            }
        }
    }

    inner class ContactHeader(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.header) lateinit var header: TextView

        init { ButterKnife.bind(this, itemView)}

        fun bind(header: String) {
            this.header.text = header
        }
    }
}