package com.example.smartmobilefactory_app_jc.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episode(
    val id: Long,
    val name: String,
    val summary: String,
    val season: Int,
    val number: Int,
    val image: String?,
) : Parcelable