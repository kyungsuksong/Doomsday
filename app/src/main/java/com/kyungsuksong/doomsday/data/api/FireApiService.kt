package com.kyungsuksong.doomsday.data.api

import com.kyungsuksong.doomsday.BuildConfig
import com.kyungsuksong.doomsday.util.convertEpochTimeToFormattedDate
import com.kyungsuksong.doomsday.util.getCurrentTimeForFire
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface FireApiService {
    @GET("/api/country/csv/{api_key}/VIIRS_SNPP_NRT/{country}/1/{date}")
    suspend fun getFiresFromViirSnppNrtSource(
        @Path("country") country: String,
        @Path("api_key") apiKey: String = BuildConfig.FireApiKey,
        @Path("date") date: String = convertEpochTimeToFormattedDate(getCurrentTimeForFire())
    ): ResponseBody

    @GET("/api/country/csv/{api_key}/VIIRS_NOAA20_NRT/{country}/1/{date}")
    suspend fun getFiresFromViirNoaa20NrtSource(
        @Path("country") country: String,
        @Path("api_key") apiKey: String = BuildConfig.FireApiKey,
        @Path("date") date: String = convertEpochTimeToFormattedDate(getCurrentTimeForFire())
    ): ResponseBody

    @GET("/api/country/csv/{api_key}/VIIRS_NOAA21_NRT/{country}/1/{date}")
    suspend fun getFiresFromViirNoaa21NrtSource(
        @Path("country") country: String,
        @Path("api_key") apiKey: String = BuildConfig.FireApiKey,
        @Path("date") date: String = convertEpochTimeToFormattedDate(getCurrentTimeForFire())
    ): ResponseBody
}