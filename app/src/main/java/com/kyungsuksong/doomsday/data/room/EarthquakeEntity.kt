package com.kyungsuksong.doomsday.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "earthquakes")
data class EarthquakeEntity(
    @ColumnInfo(name = "code")
    var code: String,

    @ColumnInfo(name = "mag")
    var mag: Double,

    @ColumnInfo(name = "place")
    var place: String,

    @ColumnInfo(name = "time")
    var time: Long,

    @ColumnInfo(name = "url")
    var url: String,

    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double,

    @ColumnInfo(name = "depth")
    var depth: Double,

    @ColumnInfo(name = "sig")
    var sig: Int,

    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String
)