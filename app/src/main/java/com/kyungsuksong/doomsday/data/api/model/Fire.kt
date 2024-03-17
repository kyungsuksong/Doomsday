package com.kyungsuksong.doomsday.data.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fire(
    val latitude: Double,
    val longitude: Double,
    val brightness: Double,
    val acquisitionDate: String,
    val acquisitionTime: String,
    val satellite: String,
    val confidence: String,
    val sourceType: Int,
    val country: String
): Parcelable