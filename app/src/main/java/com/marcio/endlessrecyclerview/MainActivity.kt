package com.marcio.endlessrecyclerview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val logTag = MainActivity::class.simpleName

    private val items = mutableListOf<String>()

    private var recyclerView: RecyclerView? = null
    private var adapter: CustomAdapter? = null
    private var listener: EndlessScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        adapter = CustomAdapter(items)
        recyclerView = findViewById(R.id.recyclerView)
        val layoutManager = LinearLayoutManager(this)

        listener = object : EndlessScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                loadMoreData(page)
            }
        }

        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = adapter
        recyclerView!!.addOnScrollListener(listener as EndlessScrollListener)

        loadMoreData()
    }

    private fun loadMoreData(page: Int = 0) {
        val size = 30
        val newItems = generateData(page, size)
        items.addAll(newItems)
        recyclerView!!.post { adapter!!.notifyItemRangeInserted(adapter!!.itemCount - 1, size) }
        Log.v(logTag, "Loading more ${newItems.size}. Total: ${items.size}")
    }

    private fun generateData(page: Int = 0, size: Int = 20): List<String> {
        val items = mutableListOf<String>()
        for (i in 1..size) {
            items.add("Data ${(page * size) + i}")
        }
        return items
    }
}
