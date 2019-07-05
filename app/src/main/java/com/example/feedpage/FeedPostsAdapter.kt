package com.example.feedpage

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.feedpage.database.Post
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class FeedPostsAdapter: RecyclerView.Adapter<FeedPostsAdapter.FeedPostsViewHolder>() {

    var feedPostList: List<Post>? = null

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): FeedPostsViewHolder {
        return FeedPostsViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.feed, null))
    }

    override fun getItemCount(): Int {
        return feedPostList?.size ?: 0
    }

    override fun onBindViewHolder(p0: FeedPostsViewHolder, p1: Int) {
        p0.bind(feedPostList!![p1])
    }

    class FeedPostsViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val postImage = view.findViewById<ImageView>(R.id.image_iv)
        val postName = view.findViewById<TextView>(R.id.post_name)
        val postDate = view.findViewById<TextView>(R.id.post_date)
        val postLikes = view.findViewById<TextView>(R.id.post_likes)
        val postShares = view.findViewById<TextView>(R.id.post_shares)
        val postViews = view.findViewById<TextView>(R.id.post_views)

        companion object {
            private val dateFormat = SimpleDateFormat("dd MMM yyyy hh:mm:ss")
        }

        init {
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.bottomMargin = convertDPToPixel(itemView.context, 12f)


            itemView.layoutParams = layoutParams
        }
        fun bind(item: Post) {
            /** set up event name */
            postName.text = item.eventName ?: ""

            /** set up event date */
            item.eventDate?.let {dateLong ->
                try {
                    postDate.visibility = View.VISIBLE

                    val date = Date(dateLong)
                    val dateString = dateFormat.format(date)
                    postDate.text = dateString

                } catch (ex: Exception) {
                    postDate.visibility = View.GONE
                }

            } ?: run {
                postDate.visibility = View.GONE
            }

            /** set up event likes */
            postLikes.text = item.eventLikesCount?.toString().orEmpty()

            /** set up event share */
            postShares.text = item.eventShareCount?.toString() ?: ""

            /** set up event views */
            postViews.text = item.eventViewsCount?.toString() ?: ""

            /** set up event image */
            Glide
                .with(view.context)
                .load(item.thumbnailImage)
                .centerCrop()
                .into(postImage)

        }
    }
}