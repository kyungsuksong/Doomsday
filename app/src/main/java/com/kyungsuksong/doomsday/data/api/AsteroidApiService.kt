package com.kyungsuksong.doomsday.data.api

import com.kyungsuksong.doomsday.BuildConfig
import retrofit2.http.Query
import com.kyungsuksong.doomsday.data.api.model.PicOfDay
import com.kyungsuksong.doomsday.util.DAY_IN_MILLIS
import com.kyungsuksong.doomsday.util.asteroid_today
import com.kyungsuksong.doomsday.util.convertEpochTimeToFormattedDate
import okhttp3.ResponseBody
import retrofit2.http.GET

interface AsteroidApiService {
    // default date is today
    @GET("/planetary/apod")
    suspend fun getPicOfDay(
        @Query("api_key") key: String = BuildConfig.AsteroidApiKey
    ): PicOfDay

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String = convertEpochTimeToFormattedDate(asteroid_today - DAY_IN_MILLIS),
        @Query("api_key") apiKey: String = BuildConfig.AsteroidApiKey,
    ): ResponseBody
}