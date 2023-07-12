package com.example.baseandroid.resource.utils

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.setupLoadMore(minItem: Int = 20, completion: () -> Unit) {
    addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if ((adapter?.itemCount ?: 0) < minItem) {
                return
            }
            val linearLayoutManager = recyclerView.layoutManager as LinearLayoutManager?
            if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == (adapter?.itemCount
                    ?: 2) - 1
            ) {
                completion()
            }
        }
    })
}