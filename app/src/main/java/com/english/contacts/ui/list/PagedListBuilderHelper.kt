package com.english.contacts.ui.list

import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource

class PagedListBuilderHelper<T> {

    var loadInitial: ((
        PageKeyedDataSource.LoadInitialParams<Int>,
        PageKeyedDataSource.LoadInitialCallback<Int, T>) -> Unit)? = null

    var loadAfter: ((
        PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, T>) -> Unit)? = null

    var loadBefore: ((
        PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, T>) -> Unit)? = null


    fun getDataSourceFactory(): DataSource.Factory<Int, T> {
        return object : DataSource.Factory<Int, T>() {
            override fun create(): DataSource<Int, T> {
                return object : PageKeyedDataSource<Int, T>() {
                    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, T>) {
                        loadInitial?.let { onInitialLoad -> onInitialLoad(params, callback) }
                    }

                    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
                        loadAfter?.let { onLoadAfter -> onLoadAfter(params, callback) }
                    }

                    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, T>) {
                        loadBefore?.let { onLoadBefore -> onLoadBefore(params, callback) }
                    }
                }
            }
        }
    }

    companion object{
        @JvmStatic
        fun getPreviousPage(page: Int): Int? = if (page > 1) page - 1 else null

        @JvmStatic
        fun getNextPage(page: Int, lastResponseCount: Int, perPage: Int): Int? =
            if (lastResponseCount == perPage) page + 1 else null
    }
}