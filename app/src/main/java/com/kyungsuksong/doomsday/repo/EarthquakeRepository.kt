package com.kyungsuksong.doomsday.repo

import android.util.Log
import com.kyungsuksong.doomsday.data.api.EarthquakeApiService
import com.kyungsuksong.doomsday.data.api.model.Earthquake
import com.kyungsuksong.doomsday.data.room.EarthquakeDao
import com.kyungsuksong.doomsday.util.convertEpochTimeToFormattedDate
import com.kyungsuksong.doomsday.util.convertToEarthquakeEntity
import com.kyungsuksong.doomsday.util.earthquake_past15Days
import com.kyungsuksong.doomsday.util.earthquake_today
import com.kyungsuksong.doomsday.util.parseEarthquakeJsonResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import javax.inject.Singleton

@Singleton
class EarthquakeRepository(
    private val apiService: EarthquakeApiService,
    private val earthquakeDao: EarthquakeDao
) {
    companion object {
        @Volatile
        var INSTANCE: EarthquakeRepository? = null

        fun getInstance(
            apiService: EarthquakeApiService,
            earthquakeDao: EarthquakeDao
        ) = INSTANCE ?: synchronized(this) {
                val instance = EarthquakeRepository(apiService, earthquakeDao)
                INSTANCE = instance
                return instance
            }
    }

    /**
     * Methods used to fetch earthquakes from Room
     */
    fun getListOfEarthquakesFromLocal(
        startMag: Double, endMag: Double, startTime: Long, endTime: Long
    ) = earthquakeDao.getListOfEarthquakes(startMag, endMag, startTime, endTime)

    /**
     * Methods used to fetch earthquakes from api
     */
    suspend fun fetchAndSaveEarthquakes() {
        /**
         * Fetches the 1 month worth of earthquake data once a day and saves in Room
         */
        withContext(Dispatchers.IO) {
            val startTime = convertEpochTimeToFormattedDate(earthquake_past15Days)
            val endTime = convertEpochTimeToFormattedDate(earthquake_today)
            val response: ResponseBody = apiService
                .getEarthquakes(
                    startTime = startTime,
                    endTime = endTime
                )

            val earthquakeList = parseEarthquakeJsonResult(
                JSONObject(response.string())
            ) as List<Earthquake>

            convertToEarthquakeEntity(earthquakeList).forEach {
                earthquakeDao.insertEarthquake(it)
            }
        }
    }
}