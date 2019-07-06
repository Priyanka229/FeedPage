package com.example.feedpage

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import com.example.feedpage.database.Post
import com.example.feedpage.database.PostDatabase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class FeedVM(application: Application): AndroidViewModel(application) {

    private val feedInteractor: FeedInteractor = FeedInteractor()
    private var rawList: List<Post>? = null
    val filterLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val loaderLiveData: MutableLiveData<Boolean> = MutableLiveData()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    /** pagination state starts */
    private val paginationMap: HashMap<Int, String> by lazy {
        HashMap<Int, String>().apply {
            put(1, "59b3f0b0100000e30b236b7e")
            put(2, "59ac28a9100000ce0bf9c236")
            put(3, "59ac293b100000d60bf9c239")
        }
    }
    private var pageNo = 1
    private var isLoading = false
    private var isLastPage = false
    companion object {
        private const val THRESHOLD_SIZE = 3
    }
    /** pagination state ends */

    /** filter state starts */
    enum class Filter(filter: Int) {
        RESET(0), DATE(1), LIKES(2), VIEWS(3), SHARES(4)
    }
    private var currentFilter = Filter.RESET
    /** filter state ends */


    /** fetch data via interactor */
    fun getDataFromInteractor() {
        if (paginationMap.containsKey(pageNo)) {
            val path = paginationMap[pageNo]
            (!path.isNullOrEmpty()).let {
                isLoading = true
                loaderLiveData.value = true

                compositeDisposable.add(
                    feedInteractor.getFeedPosts(getApplication(), path!!)
                        .subscribeOn(Schedulers.io())
                        .map {
                            for (item in it) {
                                val post = Post(
                                    item.thumbImg.orEmpty(),
                                    item.eventName.orEmpty(),
                                    item.eventDate ?: 0L,
                                    item.views ?: 0,
                                    item.likes ?: 0,
                                    item.shares ?: 0
                                )

                                PostDatabase.getAppDatabase(getApplication()).postDao().insertPost(post)
                            }
                        }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                isLoading = false
                                loaderLiveData.value = false
                                pageNo++

                                if (pageNo > paginationMap.size) {
                                    isLastPage = true
                                }
                            },
                            {
                                loaderLiveData.value = false
                                isLoading = false
                            }
                        )
                )
            }
        }
    }

    /** decomposition of view model */
    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }

    /** fetch list and apply filters */
    fun updateListAndApplyFilters(list: List<Post>) {
        rawList = list

        // apply filters
        when (currentFilter) {
            Filter.RESET -> resetFilters()
            Filter.DATE -> dateSortApply()
            Filter.LIKES -> likesSortApply()
            Filter.VIEWS -> viewsSortApply()
            Filter.SHARES -> sharesSortApply()
        }
    }

    /** handling for recycler view scroll events */
    fun notifyScroll(firstVisibleItemPosition: Int, visibleItemCount: Int, totalItemCount: Int) {
        if (!isLoading && !isLastPage) {
            if (visibleItemCount + firstVisibleItemPosition + THRESHOLD_SIZE >= totalItemCount && firstVisibleItemPosition >= 0) {
                getDataFromInteractor()
            }
        }
    }

    /** reset the filters */
    fun resetFilters() {
        currentFilter = Filter.RESET

        rawList?.apply {
            feedInteractor.getResetFeedPosts(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    filterLiveData.value = it
                }
        }
    }

    /** sort the list based on date */
    fun dateSortApply() {
        currentFilter = Filter.DATE

        rawList?.apply {
            feedInteractor.getDateFilteredFeedPosts(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    filterLiveData.value = it
                }
        }
    }

    /** sort the list based on post likes */
    fun likesSortApply() {
        currentFilter = Filter.LIKES

        rawList?.apply {
            feedInteractor.getLikesFilteredFeedPosts(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    filterLiveData.value = it
                }
        }
    }

    /** sort the list based on number of view on that post */
    fun viewsSortApply() {
        currentFilter = Filter.VIEWS

        rawList?.apply {
            feedInteractor.getViewsFilteredFeedPosts(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    filterLiveData.value = it
                }
        }
    }

    /** sort the list based on shares */
    fun sharesSortApply() {
        currentFilter = Filter.SHARES

        rawList?.apply {
            feedInteractor.getSharesFilteredFeedPosts(this)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe{
                    filterLiveData.value = it
                }
        }
    }
}