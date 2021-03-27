package com.english.contacts.ui.list

import android.view.ViewGroup
import androidx.paging.AsyncPagedListDiffer
import androidx.paging.PagedList
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.*

const val VIEW_TYPE_ITEM = 0

abstract class BaseAdapter<Item, ItemViewHolder : RecyclerView.ViewHolder>(
    protected var onItemClickListener: OnItemClickListener<Item>?,
    diffUtil: DiffUtil.ItemCallback<Item>
) : PagedListAdapter<Item, ItemViewHolder>(diffUtil) {

    protected lateinit var differ: AsyncPagedListDiffer<Item>

    val adapterCallback = AdapterListUpdateCallback(this)

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {
            adapterCallback.onInserted(position, count)
        }

        override fun onRemoved(position: Int, count: Int) {
            adapterCallback.onRemoved(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapterCallback.onMoved(fromPosition, toPosition)
        }

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapterCallback.onChanged(position, count, payload)
        }
    }

    abstract fun initDiffer()

    protected fun setDiffer(diffCallback: AsyncDifferConfig<Item>) {
        differ = AsyncPagedListDiffer<Item>(listUpdateCallback, diffCallback)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return getItemViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        onBindItemViewHolder(holder, position)
    }

    override fun onViewRecycled(holder: ItemViewHolder) {
        onViewRecycledParent(holder)
    }

    protected open fun onViewRecycledParent(holder: RecyclerView.ViewHolder) {
        //do nothing
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ITEM
    }

    override fun getItemCount(): Int {
        return differ.itemCount
    }

    public override fun getItem(position: Int): Item? {
        return differ.getItem(position)
    }

    override fun submitList(pagedList: PagedList<Item>?) {
        differ.submitList(pagedList)
    }

    override fun getCurrentList(): PagedList<Item>? {
        return differ.currentList
    }

    private fun onBindItemViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val pos = if (holder.adapterPosition >= 0) holder.adapterPosition else position
            onItemClickListener?.onListItemClick(getItem(pos), pos)
        }
        onBindItemViewHolder(
            holder,
            getItem(if (holder.adapterPosition >= 0) holder.adapterPosition else position)
        )
    }

    abstract fun onBindItemViewHolder(holder: ItemViewHolder, item: Item?)

    abstract fun getItemViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder

    interface OnItemClickListener<Item> {
        fun onListItemClick(item: Item?, position: Int)
    }
}