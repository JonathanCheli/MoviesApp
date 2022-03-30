package com.example.smartmobilefactory_app_jc.network.payload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ShowResponse(
    @Json(name = "id") val id: Long?,
    @Json(name = "name") val name: String?,
    @Json(name = "summary") val summary: String?,
    @Json(name = "image") val image: ImageResponse?,
    @Json(name = "language") val language: String?,
    @Json(name = "genres") val genres: List<String?>?,
    @Json(name = "rating") val rating: RatingResponse?,
    @Json(name = "schedule") val schedule: ScheduleResponse?,
)