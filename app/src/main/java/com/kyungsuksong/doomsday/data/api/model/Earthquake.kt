package com.kyungsuksong.doomsday.data.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Earthquake(
    val id: String,
    val code: String,
    val mag: Double,
    val place: String,
    val time: Long,
    val url: String,
    val latitude: Double,
    val longitude: Double,
    val depth: Double,
    val sig: Int
): Parcelable