package com.example.smartmobilefactory_app_jc.network.payload

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SeasonResponse(
    @Json(name = "id") val id: Long?,
    @Json(name = "number") val number: Int?,
    @Json(name = "name") val name: String?,
)