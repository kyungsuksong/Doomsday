package com.kyungsuksong.doomsday.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fires")
data class FireEntity(
    @ColumnInfo(name = "latitude")
    var latitude: Double,

    @ColumnInfo(name = "longitude")
    var longitude: Double,

    @ColumnInfo(name = "brightness")
    var brightness: Double,

    @ColumnInfo(name = "date")
    var acquisition_date: String,

    @ColumnInfo(name = "time")
    var acquisition_time: String,

    @ColumnInfo(name = "satelite")
    var satellite: String,

    @ColumnInfo(name = "confidence")
    var confidence: Double,

    @ColumnInfo(name = "source_type")
    var sourceType: Int,

    @ColumnInfo(name = "country")
    var country: String,

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long = 0
)