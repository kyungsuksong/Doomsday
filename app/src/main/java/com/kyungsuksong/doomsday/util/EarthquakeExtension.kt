package com.kyungsuksong.doomsday.util

import android.annotation.SuppressLint
import androidx.compose.ui.graphics.Color
import androidx.vectordrawable.graphics.drawable.ArgbEvaluator
import com.google.android.gms.maps.model.LatLng
import com.kyungsuksong.doomsday.data.api.model.Earthquake
import com.kyungsuksong.doomsday.data.room.EarthquakeEntity
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

val earthquake_today: Long = LocalDateTime
    .now()
    .atZone(ZoneOffset.UTC)
    .toInstant()
    .toEpochMilli()

val earthquake_past15Days: Long = earthquake_today - 1296000000

fun getMidpointLatLng(lat1: Double, lon1: Double, lat2: Double, lon2: Double): LatLng {
    val dLonRad = Math.toRadians(lon2 - lon1)

    //convert to radians
    val lat1rad = Math.toRadians(lat1)
    val lat2rad = Math.toRadians(lat2)
    val lon1rad = Math.toRadians(lon1)

    val Bx = cos(lat2rad) * cos(dLonRad)
    val By = cos(lat2rad) * sin(dLonRad)
    val lat3rad = atan2(sin(lat1rad) + sin(lat2rad), sqrt((cos(lat1rad) + Bx) * (cos(lat1rad) + Bx) + By * By))
    val lon3rad: Double = lon1rad + atan2(By, cos(lat1rad) + Bx)

    return LatLng(Math.toDegrees(lat3rad), Math.toDegrees(lon3rad))
}


fun pointBetweenColors(percent: Double): Color {
    var percentage = percent
    percentage = Math.min(percentage, 1.0)
    percentage = Math.max(percentage, 0.0)

    val red = (255.0 * (percentage)).toInt()
    val green = (255.0 * (1 - percentage)).toInt()
    val blue = 0
    return Color(red, green, blue)
}

/**
 * Methods used read JSON file and convert to earthquake entity
 */
fun parseEarthquakeJsonResult(jsonResult: JSONObject): ArrayList<Earthquake> {
    val featuresJsonArray = jsonResult.getJSONArray("features")

    val earthquakeList = ArrayList<Earthquake>()

    for (i in 0 until featuresJsonArray.length()) {
        val earthquakeJson = featuresJsonArray.getJSONObject(i)

        val id = earthquakeJson.getString("id")

        val propertiesJson = earthquakeJson.getJSONObject("properties")
        val code = propertiesJson.optString("code")
        val mag = propertiesJson.optDouble("mag")
        val place = propertiesJson.optString("place")
        val time = propertiesJson.optLong("time")
        val url = propertiesJson.optString("url")
        val sig = propertiesJson.optInt("sig")

        val geometryJson = earthquakeJson.getJSONObject("geometry")
        val coordinatesJsonArray = geometryJson.getJSONArray("coordinates")
        val longitude = coordinatesJsonArray.optDouble(0)
        val latitude = coordinatesJsonArray.optDouble(1)
        val depth = coordinatesJsonArray.optDouble(2)

        val earthquake = Earthquake(
            id, code, mag, place, time, url, latitude, longitude, depth, sig
        )

        earthquakeList.add(earthquake)
    }

    return earthquakeList
}

fun convertToEarthquakeEntity(list: List<Earthquake>): List<EarthquakeEntity> {
    val res = ArrayList<EarthquakeEntity>()
    res.addAll(list.map {
        EarthquakeEntity(
            it.code,
            it.mag,
            it.place,
            it.time,
            it.url,
            it.latitude,
            it.longitude,
            it.depth,
            it.sig,
            it.id
        )
    })
    return res
}