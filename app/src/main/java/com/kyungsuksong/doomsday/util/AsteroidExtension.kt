package com.kyungsuksong.doomsday.util

import android.annotation.SuppressLint
import com.kyungsuksong.doomsday.data.api.model.Asteroid
import com.kyungsuksong.doomsday.data.room.AsteroidEntity
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.floor

val asteroid_today: Long = LocalDateTime
    .now()
    .atZone(ZoneOffset.UTC)
    .toInstant()
    .toEpochMilli()

val asteroid_tomorrow: Long = asteroid_today + DAY_IN_MILLIS

@SuppressLint("SimpleDateFormat")
fun convertEpochTimeToFormattedDate(epochTimeInMillis: Long): String {
    return SimpleDateFormat("yyyy-MM-dd").format(Date(epochTimeInMillis))
}

@SuppressLint("SimpleDateFormat", "WeekBasedYear")
fun convertDateToEpochTime(dateStr: String): Long {
    return try {
        LocalDate
            .parse(dateStr)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    } catch (e: Exception) {
        -1L
    }
}

fun getCurrentTimeMillisInUTC(): Long {
    return LocalDateTime
        .now()
        .atZone(ZoneOffset.UTC)
        .toInstant()
        .toEpochMilli()
}

fun getAsteroidDaysString(asteroidEntity: AsteroidEntity): String {
    // use floor() to floor negative values
    val days = floor(
        (asteroidEntity.closeApproachDate.toDouble() - getCurrentTimeMillisInUTC().toDouble())/DAY_IN_MILLIS.toDouble()
    )
    // Log.d("AndroidScreen", "days: " + days)
    return if (days < -1.0) {
        "${-days.toLong()} days ago"
    } else if (days == -1.0) {
        "Yesterday"
    } else if (days == 0.0) {
        "Today"
    } else if (days == 1.0) {
        "Tomorrow"
    } else {
        "In ${days.toLong()} days"
    }
}

/**
 * Methods used read JSON file and convert to asteroid entity
 */
fun parseAsteroidJsonResult(jsonResult: JSONObject): ArrayList<Asteroid> {
    val nearEarthObjectsJson = jsonResult.getJSONObject("near_earth_objects")

    val asteroidList = ArrayList<Asteroid>()

    val nextSevenDaysFormattedDates = getNextSevenDaysFormattedDates()

    for (formattedDate in nextSevenDaysFormattedDates) {
        val dateAsteroidJsonArray = nearEarthObjectsJson.optJSONArray(formattedDate)

        if (dateAsteroidJsonArray != null) {
            for (i in 0 until dateAsteroidJsonArray.length()) {
                val asteroidJson = dateAsteroidJsonArray.getJSONObject(i)
                val id = asteroidJson.getLong("id")
                val codename = asteroidJson.getString("name")
                val absoluteMagnitude = asteroidJson.getDouble("absolute_magnitude_h")
                val estimatedDiameterMin = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("feet").getDouble("estimated_diameter_min")
                val estimatedDiameterMax = asteroidJson.getJSONObject("estimated_diameter")
                    .getJSONObject("feet").getDouble("estimated_diameter_max")

                val closeApproachData = asteroidJson
                    .getJSONArray("close_approach_data").getJSONObject(0)
                val epochDateCloseApproach = closeApproachData.getLong("epoch_date_close_approach")
                val relativeVelocity = closeApproachData.getJSONObject("relative_velocity")
                    .getDouble("miles_per_hour")
                val distanceFromEarthInAU = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("astronomical")
                val distanceFromEarthInMiles = closeApproachData.getJSONObject("miss_distance")
                    .getDouble("miles")
                val isPotentiallyHazardous = asteroidJson
                    .getBoolean("is_potentially_hazardous_asteroid")

                val asteroid = Asteroid(
                    id, codename, epochDateCloseApproach, absoluteMagnitude, estimatedDiameterMin, estimatedDiameterMax,
                    relativeVelocity, distanceFromEarthInAU, distanceFromEarthInMiles, isPotentiallyHazardous
                )
                asteroidList.add(asteroid)
            }
        }
    }

    return asteroidList
}

private fun getNextSevenDaysFormattedDates(): ArrayList<String> {
    val formattedDateList = ArrayList<String>()
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = asteroid_today // use epoch time as ref

    for (i in 0..7) {
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(API_QUERY_DATE_FORMAT, Locale.getDefault())
        formattedDateList.add(dateFormat.format(currentTime))
        // Log.d("Extension", "date: " + dateFormat.format(currentTime))
        calendar.add(Calendar.DAY_OF_YEAR, 1)
    }

    return formattedDateList
}

fun convertToAsteroidEntity(list: List<Asteroid>): List<AsteroidEntity> {
    val res = ArrayList<AsteroidEntity>()
    res.addAll(list.map {
        AsteroidEntity(
            it.codename,
            it.closeApproachDate,
            it.absoluteMagnitude,
            it.estimatedDiameterMin,
            it.estimatedDiameterMax,
            it.isPotentiallyHazardous,
            it.relativeVelocity,
            it.distanceFromEarthInAU,
            it.distanceFromEarthInMiles,
            it.id
        )
    })
    return res
}