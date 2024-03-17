package com.kyungsuksong.doomsday.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "asteroids")
data class AsteroidEntity(
    @ColumnInfo(name = "codename")
    var codename: String,

    @ColumnInfo(name = "close_approach_date")
    var closeApproachDate: Long,

    @ColumnInfo(name = "absolute_magnitude")
    var absoluteMagnitude: Double,

    @ColumnInfo(name = "estimated_diameter_min")
    var estimatedDiameterMin: Double,

    @ColumnInfo(name = "estimated_diameter_max")
    var estimatedDiameterMax: Double,

    @ColumnInfo(name = "is_potentially_hazardous")
    var isPotentiallyHazardous: Boolean,

    @ColumnInfo(name = "relative_velocity")
    var relativeVelocity: Double,

    @ColumnInfo(name = "distance_from_earth_in_au")
    var distanceFromEarthInAU: Double,

    @ColumnInfo(name = "distance_from_earth_in_miles")
    var distanceFromEarthInMiles: Double,

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long
)