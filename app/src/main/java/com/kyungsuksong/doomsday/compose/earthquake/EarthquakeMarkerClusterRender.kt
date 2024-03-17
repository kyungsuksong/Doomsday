package com.kyungsuksong.doomsday.compose.earthquake

import android.annotation.SuppressLint
import android.content.Context
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.Cluster
import com.google.maps.android.clustering.ClusterItem
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.clustering.view.DefaultClusterRenderer

class EarthquakeMarkerClusterRender<T : ClusterItem>(
    var context: Context,
    private var googleMap: GoogleMap,
    clusterManager: ClusterManager<T>,
    var onInfoWindowClick: (EarthquakeItem) -> Unit
): DefaultClusterRenderer<T>(context, googleMap, clusterManager) {
    private var clusterMap: HashMap<String, Marker> = hashMapOf()

    override fun shouldRenderAsCluster(cluster: Cluster<T>): Boolean = cluster.size > 1

    override fun getBucket(cluster: Cluster<T>): Int = cluster.size

    override fun getClusterText(bucket: Int): String =
        super.getClusterText(bucket).replace("+", "")

    override fun onClusterItemRendered(clusterItem: T, marker: Marker) {
        super.onClusterItemRendered(clusterItem, marker)
        clusterMap[(clusterItem as EarthquakeItem).earthquakeEntity.id] = marker
        setMarker((clusterItem as EarthquakeItem), marker)
    }

    @SuppressLint("PotentialBehaviorOverride")
    private fun setMarker(poi: EarthquakeItem, marker: Marker?) {
        val markerColor = BitmapDescriptorFactory.HUE_RED
        marker?.let {
            it.tag = poi
            it.showInfoWindow()
            changeMarkerColor(it, markerColor)
        }
        googleMap.setOnInfoWindowClickListener {
            onInfoWindowClick(it.tag as EarthquakeItem)
        }
    }

    private fun getClusterMarker(itemId: String): Marker? {
        return if (clusterMap.containsKey(itemId)) clusterMap[itemId]
        else null
    }

    fun showRouteInfoWindow(key: String) {
        getClusterMarker(key)?.showInfoWindow()
    }

    private fun changeMarkerColor(marker: Marker, color: Float) {
        try {
            marker.setIcon(BitmapDescriptorFactory.defaultMarker(color))
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun changeMarkerColorToSelected(key: String) {
        val markerColor = BitmapDescriptorFactory.HUE_GREEN
        clusterMap[key]?.let{
            changeMarkerColor(it, markerColor)
        }
    }

    fun changeMarkerColorToUnselected(key: String) {
        val markerColor = BitmapDescriptorFactory.HUE_RED
        clusterMap[key]?.let{
            changeMarkerColor(it, markerColor)
        }
    }
}