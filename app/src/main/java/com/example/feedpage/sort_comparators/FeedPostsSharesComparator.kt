package com.example.feedpage.sort_comparators

import com.example.feedpage.database.Post

class FeedPostsSharesComparator: Comparator<Post>
{
    override fun compare(r1: Post, r2: Post): Int {
        val shares1 = r1.eventShareCount!!.toInt()
        val shares2 = r2.eventShareCount!!.toInt()

        return when {
            shares1 > shares2 -> -1
            shares1 < shares2 -> 1
            else -> 0
        }
    }

}
