package com.example.smartmobilefactory_app_jc.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Season(
    val id: Long,
    val number: Int,
    val name: String,
) : Parcelable
