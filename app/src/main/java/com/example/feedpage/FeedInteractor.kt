package com.example.feedpage

import android.content.Context
import com.example.feedpage.database.Post
import com.example.feedpage.network.RetroFitClient
import com.example.feedpage.sort_comparators.FeedPostsDateComparator
import com.example.feedpage.sort_comparators.FeedPostsLikesComparator
import com.example.feedpage.sort_comparators.FeedPostsSharesComparator
import com.example.feedpage.sort_comparators.FeedPostsViewsComparator
import io.reactivex.Observable
import java.util.*

class FeedInteractor {

    /** retrieve list from server */
    fun getFeedPosts(context: Context, path: String): Observable<List<FeedItem>> {
        return RetroFitClient.getRetroFitService(context).getFeed(path)
            .map { it.feedList }

    }

    /** Original list */
    fun getResetFeedPosts(list: List<Post>): Observable<List<Post>> {
        return Observable.create<List<Post>> {
            it.onNext(list.toMutableList())
        }
    }

    /** likes based filtered list */
    fun getLikesFilteredFeedPosts(list: List<Post>): Observable<List<Post>> {
        return Observable.create<List<Post>> {emitter ->
            val sortedList = list.map {
                it.eventLikesCount = it.eventLikesCount ?: 0
                it
            }.toMutableList()

            Collections.sort(sortedList, FeedPostsLikesComparator())
            emitter.onNext(sortedList)
        }
    }

    /** views based filtered list */
    fun getViewsFilteredFeedPosts(list: List<Post>): Observable<List<Post>> {
        return Observable.create<List<Post>> {emitter ->
            val sortedList = list.map {
                it.eventViewsCount = it.eventViewsCount ?: 0
                it
            }.toMutableList()
            Collections.sort(sortedList, FeedPostsViewsComparator())
            emitter.onNext(sortedList)
        }
    }

    /** shares based filtered list */
    fun getSharesFilteredFeedPosts(list: List<Post>): Observable<List<Post>> {
        return Observable.create<List<Post>> {emitter ->
            val sortedList = list.map {
                it.eventShareCount = it.eventShareCount ?: 0
                it
            }.toMutableList()
            Collections.sort(sortedList, FeedPostsSharesComparator())
            emitter.onNext(sortedList)
        }
    }

    /** date wise filtered list */
    fun getDateFilteredFeedPosts(list: List<Post>): Observable<List<Post>> {
        return Observable.create<List<Post>> {emitter ->
            val sortedList = list.map {
                it.eventDate = it.eventDate ?: 0L
                it
            }.toMutableList()
            Collections.sort(sortedList, FeedPostsDateComparator())
            emitter.onNext(sortedList)
        }
    }
}