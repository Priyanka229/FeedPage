package com.example.feedpage

import com.google.gson.annotations.SerializedName

data class FeedModel (
    @SerializedName("posts") val feedList: List<FeedItem>?
)

data class FeedItem (
    @SerializedName("id") val id: String?,
    @SerializedName("thumbnail_image") val thumbImg: String? = "",
    @SerializedName("event_name") val eventName: String? = "",
    @SerializedName("event_date") val eventDate: Long? = 0,
    @SerializedName("views") val views: Int? = 0,
    @SerializedName("likes") val likes: Int? = 0,
    @SerializedName("shares") val shares: Int? = 0
)