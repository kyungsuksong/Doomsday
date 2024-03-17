package com.kyungsuksong.doomsday.compose.earthquake

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.kyungsuksong.doomsday.data.room.EarthquakeEntity
import com.kyungsuksong.doomsday.util.convertEpochTimeToFormattedDate

data class EarthquakeItem(
    val earthquakeEntity: EarthquakeEntity
): ClusterItem {
    override fun getPosition(): LatLng = LatLng(earthquakeEntity.latitude, earthquakeEntity.longitude)

    override fun getTitle(): String = earthquakeEntity.place

    override fun getSnippet(): String = getEarthquakeClusterItemSnippet(earthquakeEntity)

    override fun getZIndex(): Float = 0.0f

    // current clustering featuredoesn't allow customization of the InfoWindow
    /* private fun getEarthquakeClusterItemSnippet(earthquakeEntity: EarthquakeEntity): String {
        return "magnitude: " + earthquakeEntity.mag +
                "\ndate: " + convertEpochTimeToFormattedDate(earthquakeEntity.time) + " UST" +
                "\nurl: " + earthquakeEntity.url
    } */

    private fun getEarthquakeClusterItemSnippet(earthquakeEntity: EarthquakeEntity): String {
        return earthquakeEntity.url
    }
}

