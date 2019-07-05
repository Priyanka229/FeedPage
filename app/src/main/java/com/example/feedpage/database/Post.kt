package com.example.feedpage.database

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.arch.persistence.room.ColumnInfo
import org.jetbrains.annotations.NotNull

@Entity
class Post {

    constructor()
    constructor(thumbnailImage: String, eventName: String, eventDate: Long, viewsCount: Int, likesCount: Int, shareCount: Int) {
        this.thumbnailImage = thumbnailImage
        this.eventName = eventName
        this.eventDate = eventDate
        this.eventViewsCount = viewsCount
        this.eventLikesCount = likesCount
        this.eventShareCount = shareCount
    }

    var postId: Int = 0

    @ColumnInfo(name = "thumbnail_image")
    var thumbnailImage: String? = null

    @PrimaryKey
    @NotNull
    @ColumnInfo(name = "event_name")
    var eventName: String? = null

    @ColumnInfo(name = "event_date")
    var eventDate: Long? = null

    @ColumnInfo(name = "views")
    var eventViewsCount: Int? = null

    @ColumnInfo(name = "likes")
    var eventLikesCount: Int? = null

    @ColumnInfo(name = "shares")
    var eventShareCount: Int? = null



}