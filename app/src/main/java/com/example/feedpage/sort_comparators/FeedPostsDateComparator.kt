package com.example.feedpage.sort_comparators

import com.example.feedpage.database.Post

class FeedPostsDateComparator: Comparator<Post>
{
    override fun compare(r1: Post, r2: Post): Int {
        val date1 = r1.eventDate!!.toLong()
        val date2 = r2.eventDate!!.toLong()

        return when {
            date1 > date2 -> -1
            date1 < date2 -> 1
            else -> 0
        }
    }

}
