package com.example.feedpage.network

import com.example.feedpage.FeedModel
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RetroFitApiServices {
    @GET("{path}")
    fun getFeed(@Path("path") path: String): Observable<FeedModel>
}