package com.kyungsuksong.doomsday.compose.earthquake

import android.annotation.SuppressLint
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.ClusterManager.OnClusterItemClickListener
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.collections.MarkerManager
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.google.maps.android.compose.rememberCameraPositionState
import com.kyungsuksong.doomsday.R
import com.kyungsuksong.doomsday.data.room.EarthquakeEntity
import com.kyungsuksong.doomsday.util.Dimens
import com.kyungsuksong.doomsday.util.EARTHQUAKE_MAP_CLUSTERING_TAG
import com.kyungsuksong.doomsday.util.getMidpointLatLng
import com.kyungsuksong.doomsday.util.pointBetweenColors

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun EarthquakeMapClustering(
    itemList: List<EarthquakeItem>,
    modifier: Modifier = Modifier,
    currEarthquakeEntity: EarthquakeEntity?,
    prevEarthquakeEntity: EarthquakeEntity?,
    colorClusterContent: (Collection<EarthquakeItem>) -> Color,
    onClusterItemClick: (EarthquakeItem) -> Unit
) {
    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = false,
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            LatLng(
                currEarthquakeEntity?.latitude ?: 0.0,
                currEarthquakeEntity?.longitude ?: 0.0
            ),
            1.0f,
            0f,
            0f
        )
    }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.mapHeight),
        properties = mapProperties,
        cameraPositionState = cameraPositionState
    ) {
        MapEffect(key1 = currEarthquakeEntity) {
            if (currEarthquakeEntity != null) {
                val midpoint = getMidpointLatLng(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude,
                    currEarthquakeEntity.latitude,
                    currEarthquakeEntity.longitude)
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(
                                midpoint.latitude,
                                midpoint.longitude
                            ),
                            1.0f,
                            0f,
                            0f
                        )
                    ),
                    durationMs = 1000
                )
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(
                                currEarthquakeEntity.latitude,
                                currEarthquakeEntity.longitude
                            ),
                            12.0f,
                            0f,
                            0f
                        )
                    ),
                    durationMs = 1000
                )
            }
        }

        CustomRendererClustering(
            items = itemList,
            modifier = modifier,
            colorClusterContent = colorClusterContent,
            onClusterItemClick = onClusterItemClick,
            currEarthquakeEntity = currEarthquakeEntity
        )
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun CustomRendererClustering(
    items: List<EarthquakeItem>,
    modifier: Modifier = Modifier,
    colorClusterContent: (Collection<EarthquakeItem>) -> Color,
    onClusterItemClick: (EarthquakeItem) -> Unit,
    currEarthquakeEntity: EarthquakeEntity?
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val clusterManager = rememberClusterManager<EarthquakeItem>()

    // Here the clusterManager is being customized with a NonHierarchicalViewBasedAlgorithm.
    // This speeds up by a factor the rendering of items on the screen.
    clusterManager?.setAlgorithm(
        NonHierarchicalViewBasedAlgorithm(
            screenWidth.value.toInt(),
            screenHeight.value.toInt()
        )
    )
    val renderer = rememberClusterRenderer(
        clusterContent = { cluster ->
            ClusterContent(
                modifier = modifier.size(50.dp),
                text = "%,d".format(cluster.size),
                color = colorClusterContent(cluster.items)
            )
        },
        clusterItemContent = {
            ClusterItemContent(
                modifier = modifier.size(25.dp),
                color = pointBetweenColors(Math.min(it.earthquakeEntity.mag, 10.0)/10.0),
                // selected = currEarthquakeEntity?.id.equals(it.earthquakeEntity.id)
            )
        },
        clusterManager = clusterManager,
    )
    SideEffect {
        clusterManager ?: return@SideEffect
        clusterManager.setOnClusterClickListener {
            Log.d(EARTHQUAKE_MAP_CLUSTERING_TAG, "Cluster clicked! $it")
            false
        }
        clusterManager.setOnClusterItemClickListener {
            onClusterItemClick(it)
            false// true disables cluster item click
        }
        clusterManager.setOnClusterItemInfoWindowClickListener {
            Log.d(EARTHQUAKE_MAP_CLUSTERING_TAG, "Cluster item info window clicked! $it")
        }
        clusterManager.markerCollection.setInfoWindowAdapter(object : InfoWindowAdapter {
            @SuppressLint("InflateParams")
            var mWindow: View = LayoutInflater
                .from(context)
                .inflate(R.layout.earthquake_info_window, null)

            private fun setInfoWindowText(marker: Marker) {
                val title = mWindow.findViewById<TextView>(R.id.title)
                title.text = "test"

                val body = mWindow.findViewById<TextView>(R.id.body)
                body.text = "test"
            }

            override fun getInfoWindow(p0: Marker): View {
                setInfoWindowText(p0)
                return mWindow
            }

            override fun getInfoContents(p0: Marker): View {
                setInfoWindowText(p0)
                return mWindow
            }
        })
    }
    SideEffect {
        if (clusterManager?.renderer != renderer) {
            clusterManager?.renderer = renderer ?: return@SideEffect
        }
    }
    LaunchedEffect(currEarthquakeEntity) {
        clusterManager ?: return@LaunchedEffect
        clusterManager.markerCollection.markers.forEach {
            if (currEarthquakeEntity?.latitude?.equals(it.position.latitude) == true &&
                currEarthquakeEntity.longitude.equals(it.position.longitude)) {
                it.showInfoWindow()
            }
        }
    }

    if (clusterManager != null) {
        Clustering(
            items = items,
            clusterManager = clusterManager,
        )
    }
}

@Composable
private fun ClusterContent(
    color: Color,
    text: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier,
        shape = CircleShape,
        color = color,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ClusterItemContent(
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier,
        shape = CircleShape,
        color = color,
        contentColor = Color.White,
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = "",
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}