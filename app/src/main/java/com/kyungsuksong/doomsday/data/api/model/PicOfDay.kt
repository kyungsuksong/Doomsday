package com.kyungsuksong.doomsday.data.api.model

import com.squareup.moshi.Json

class PicOfDay(
    @Json(name = "media_type")
    val mediaType: String,
    val title: String,
    val url: String
)