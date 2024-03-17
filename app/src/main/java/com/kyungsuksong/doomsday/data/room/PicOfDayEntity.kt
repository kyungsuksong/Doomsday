package com.kyungsuksong.doomsday.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pictures")
data class PicOfDayEntity(
    @ColumnInfo(name = "media_type")
    var mediaType: String,

    @ColumnInfo(name = "image_title")
    var title: String,

    @ColumnInfo(name = "image_url")
    var url: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null
}