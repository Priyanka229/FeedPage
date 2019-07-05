package com.example.feedpage

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import com.example.feedpage.database.PostDatabase


class MainActivity : AppCompatActivity() {

    private val recyclerView by lazy { findViewById<RecyclerView>(R.id.recyclerview) }
    private val progressBar by lazy { findViewById<ProgressBar>(R.id.progress_bar) }
    private val viewModel by lazy { ViewModelProviders.of(this).get(FeedVM::class.java) }
    private val linearLayoutManager = LinearLayoutManager(this@MainActivity)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val feedPostsAdapter = FeedPostsAdapter()
        recyclerView.apply {
            layoutManager = linearLayoutManager
            adapter = feedPostsAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener(){
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    val visibleItemCount = linearLayoutManager.childCount
                    val totalItemCount = linearLayoutManager.itemCount
                    val firstVisibleItemPosition = linearLayoutManager.findFirstVisibleItemPosition()

                    viewModel.notifyScroll(firstVisibleItemPosition, visibleItemCount, totalItemCount)
                }
            })
        }

        /** room database observer */
        PostDatabase.getAppDatabase(this).postDao().getPost().observe(this, Observer {
            it?.apply {
                viewModel.updateListAndApplyFilters(this)
            }
        })

        /** loader observer */
        viewModel.loaderLiveData.observe(this, Observer {
            it?.let {showLoader ->
                progressBar.visibility = if (showLoader) View.VISIBLE else  View.GONE
            } ?: run {
                progressBar.visibility = View.GONE
            }
        })

        /** filtered data observer */
        viewModel.filterLiveData.observe(this, Observer {
            it?.apply {
                feedPostsAdapter.feedPostList = this
                feedPostsAdapter.notifyDataSetChanged()
            }
        })

        viewModel.getDataFromInteractor()
    }

    /** filter's view inflation */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.feed_menu, menu)
        return true
    }

    /** filter selection handling */
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.reset -> {
                viewModel.resetFilters()
                true
            }
            R.id.date_sort -> {
                viewModel.dateSortApply()
                true
            }
            R.id.likes_sort -> {
                viewModel.likesSortApply()
                true
            }
            R.id.views_sort -> {
                viewModel.viewsSortApply()
                true
            }
            R.id.shares_sort -> {
                viewModel.sharesSortApply()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}