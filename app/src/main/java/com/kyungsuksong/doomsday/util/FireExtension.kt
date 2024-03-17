package com.kyungsuksong.doomsday.util

import android.util.Log
import com.kyungsuksong.doomsday.data.api.model.Earthquake
import com.kyungsuksong.doomsday.data.api.model.Fire
import com.kyungsuksong.doomsday.data.room.FireEntity
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.ZoneOffset

fun getCurrentTimeForFire(): Long = LocalDateTime
    .now()
    .atZone(ZoneOffset.UTC)
    .toInstant()
    .toEpochMilli()

fun getYesterdayTimeForFire(): Long = getCurrentTimeForFire() - 86400000L

fun parseFireResult(result: String, sourceType: Int): List<Fire> {
    val dataSplitByNewLine: List<String> = result.split("\n")

    val fireList = ArrayList<Fire>()

    for (i in 1 until dataSplitByNewLine.size) {
        try {
            val data = dataSplitByNewLine[i].split(",")
            val country: String = data[0]
            val latitude: Double = data[1].toDouble()
            val longitude: Double = data[2].toDouble()
            val brightness: Double = data[3].toDouble()
            val acquisitionDate: String = data[6]
            val acquisitionTime: String = data[7]
            val satellite: String = data[8]
            val confidence: String = data[10]

            fireList.add(
                Fire(latitude, longitude, brightness, acquisitionDate, acquisitionTime, satellite, confidence, sourceType, country)
            )
        } catch (e: Exception) {
            Log.d("FireExtension", e.toString())
        }
    }
    return fireList
}

fun convertToFireEntity(list: List<Fire>): List<FireEntity> {
    val res = ArrayList<FireEntity>()
    res.addAll(list.map {
        FireEntity(
            latitude = it.latitude,
            longitude = it.longitude,
            brightness = it.brightness,
            acquisition_date = it.acquisitionDate,
            acquisition_time = it.acquisitionTime,
            satellite = it.satellite,
            confidence = convertConfidenceStingToDouble(it.confidence),
            sourceType = it.sourceType,
            country = it.country
        )
    })
    return res
}

private fun convertConfidenceStingToDouble(confidence: String): Double {
    return when(confidence) {
        "l" -> 0.0
        "n" -> 5.0
        "h" -> 10.0
        else -> 0.0
    }
}