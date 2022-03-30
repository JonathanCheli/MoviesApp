package com.example.smartmobilefactory_app_jc.network.api

import com.example.smartmobilefactory_app_jc.network.payload.EpisodeResponse
import com.example.smartmobilefactory_app_jc.network.payload.SearchResponse
import com.example.smartmobilefactory_app_jc.network.payload.SeasonResponse
import com.example.smartmobilefactory_app_jc.network.payload.ShowResponse
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TvShowService {

    @GET("shows")
    fun shows(
        @Query("page") page: Int? = null,
    ): Single<List<ShowResponse>>

    @GET("search/shows")
    fun search(
        @Query("q") query: String,
    ): Single<List<SearchResponse>>

    @GET("/shows/{showId}/seasons")
    fun seasons(
        @Path("showId") showId: Long,
    ): Single<List<SeasonResponse>>

    @GET("/seasons/{seasonId}/episodes")
    fun episodes(
        @Path("seasonId") seasonId: Long,
    ): Single<List<EpisodeResponse>>

}
