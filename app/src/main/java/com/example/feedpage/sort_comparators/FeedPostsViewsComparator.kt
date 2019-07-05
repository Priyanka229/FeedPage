package com.example.feedpage.sort_comparators

import com.example.feedpage.database.Post

class FeedPostsViewsComparator: Comparator<Post>
{
    override fun compare(r1: Post, r2: Post): Int {
        val views1 = r1.eventViewsCount!!.toInt()
        val views2 = r2.eventViewsCount!!.toInt()

        return when {
            views1 > views2 -> -1
            views1 < views2 -> 1
            else -> 0
        }
    }

}
