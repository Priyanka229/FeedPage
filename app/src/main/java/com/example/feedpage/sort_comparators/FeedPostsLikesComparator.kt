package com.example.feedpage.sort_comparators

import com.example.feedpage.database.Post

class FeedPostsLikesComparator: Comparator<Post>
{
    override fun compare(r1: Post, r2: Post): Int {
        val likes1 = r1.eventLikesCount!!.toInt()
        val likes2 = r2.eventLikesCount!!.toInt()

        return when {
            likes1 > likes2 -> -1
            likes1 < likes2 -> 1
            else -> 0
        }
    }

}
