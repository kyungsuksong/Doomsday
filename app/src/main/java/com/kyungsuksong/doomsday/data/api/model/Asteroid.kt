package com.kyungsuksong.doomsday.data.api.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Asteroid(
    val id: Long,
    val codename: String,
    val closeApproachDate: Long,
    val absoluteMagnitude: Double,
    val estimatedDiameterMin: Double,
    val estimatedDiameterMax: Double,
    val relativeVelocity: Double,
    val distanceFromEarthInAU: Double,
    val distanceFromEarthInMiles: Double,
    val isPotentiallyHazardous: Boolean
): Parcelable