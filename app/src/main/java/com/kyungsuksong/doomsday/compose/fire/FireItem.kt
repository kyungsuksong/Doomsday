package com.kyungsuksong.doomsday.compose.fire

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.clustering.ClusterItem
import com.kyungsuksong.doomsday.data.room.FireEntity

data class FireItem(
    val fireEntity: FireEntity
): ClusterItem {
    override fun getPosition(): LatLng = LatLng(fireEntity.latitude, fireEntity.longitude)

    override fun getTitle(): String = fireEntity.acquisition_date

    override fun getSnippet(): String = "Brightness (K): " + fireEntity.brightness

    override fun getZIndex(): Float?  = 0.0f
}