package com.example.core.utils

import android.os.AsyncTask
import android.os.Handler
import android.os.Looper
import androidx.paging.PagedList
import androidx.paging.PositionalDataSource
import java.util.concurrent.Executor

class PagedListConverter<T> {

    fun convert(list: List<T>, pageSize: Int): PagedList<T> {
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(pageSize)
            .build()

        return PagedList.Builder(ListDataSource(list), config)
            .setNotifyExecutor(UiThreadExecutor())
            .setFetchExecutor(AsyncTask.THREAD_POOL_EXECUTOR)
            .build()
    }

    private class ListDataSource<T>(private val items: List<T>) : PositionalDataSource<T>() {
        override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
            callback.onResult(items, 0, items.size)
        }

        override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
            val start = params.startPosition
            val end = params.startPosition + params.loadSize
            callback.onResult(items.subList(start, if (end > items.size) items.size else end))
        }
    }

    private class UiThreadExecutor : Executor {
        private val handler = Handler(Looper.getMainLooper())
        override fun execute(command: Runnable) {
            handler.post(command)
        }
    }
}