package com.marcio.endlessrecyclerview

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private val logTag = MainActivity::class.simpleName

    private val db = mutableListOf<String>()
    private val items = mutableListOf<String>()

    private var recyclerView: RecyclerView? = null
    private var adapter: CustomAdapter? = null
    private var listener: EndlessScrollListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initFakeDB()
        loadMoreData()

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
    }

    private fun loadMoreData(page: Int = 0) {
        val limit = 20
        val offset = page * limit

        val moreItems = loadFromFakeDB(offset, limit)

        if (moreItems.isNotEmpty()) {
            items.addAll(moreItems)
            Log.v(logTag, "Loading more ${moreItems.size}. Total: ${items.size}")
            if (offset > 0) {
                recyclerView!!.post { adapter!!.notifyItemRangeInserted(offset, moreItems.size) }
            }
        }
    }

    private fun initFakeDB() {
        for (i in 1..1000) {
            db.add("Data $i")
        }
    }

    private fun loadFromFakeDB(offset: Int, limit: Int): List<String> {
        val pagedItems = mutableListOf<String>()

        if (offset < db.size) {
            val nextOffset = offset + limit
            for (i in offset until if (nextOffset < db.size) nextOffset else db.size) {
                pagedItems.add(db[i])
            }
        }

        return pagedItems
    }
}
