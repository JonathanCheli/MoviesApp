package com.example.smartmobilefactory_app_jc.network.payload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class EpisodeResponse(
    @Json(name = "id") val id: Long?,
    @Json(name = "season") val season: Int?,
    @Json(name = "number") val number: Int?,
    @Json(name = "image") val image: ImageResponse?,
    @Json(name = "name") val name: String?,
    @Json(name = "summary") val summary: String?,
)