package com.kyungsuksong.doomsday.compose.fire

import android.annotation.SuppressLint
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.clustering.algo.NonHierarchicalViewBasedAlgorithm
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapEffect
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapsComposeExperimentalApi
import com.google.maps.android.compose.clustering.Clustering
import com.google.maps.android.compose.clustering.rememberClusterManager
import com.google.maps.android.compose.clustering.rememberClusterRenderer
import com.google.maps.android.compose.rememberCameraPositionState
import com.kyungsuksong.doomsday.R
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeItem
import com.kyungsuksong.doomsday.data.room.FireEntity
import com.kyungsuksong.doomsday.util.Dimens
import com.kyungsuksong.doomsday.util.FIRE_MAP_CLUSTERING_TAG
import com.kyungsuksong.doomsday.util.getMidpointLatLng
import com.kyungsuksong.doomsday.util.pointBetweenColors
import com.kyungsuksong.doomsday.viewmodel.FireViewModel

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun FireMapClustering(
    countryLatLng: LatLng?,
    itemList: List<FireItem>,
    modifier: Modifier = Modifier,
    currFireEntity: FireEntity?,
    prevFireEntity: FireEntity?,
    colorClusterContent: (Collection<FireItem>) -> Color,
    onClusterItemClick: (FireItem) -> Unit
) {
    // Set properties using MapProperties which you can use to recompose the map
    val mapProperties = MapProperties(
        // Only enable if user has accepted location permissions.
        isMyLocationEnabled = false,
    )

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition(
            LatLng(0.0,0.0
            ),
            1.0f,
            0f,
            0f
        )
    }

    var moveToFireCountry: Boolean by remember { mutableStateOf(false) }

    GoogleMap(
        modifier = modifier
            .fillMaxWidth()
            .height(Dimens.mapHeight),
        properties = mapProperties,
        cameraPositionState = cameraPositionState,
        onMapLoaded = {
            moveToFireCountry = true
        }
    ) {
        MapEffect(key1 = currFireEntity) {
            if (currFireEntity != null) {
                val midpoint = getMidpointLatLng(
                    cameraPositionState.position.target.latitude,
                    cameraPositionState.position.target.longitude,
                    currFireEntity.latitude,
                    currFireEntity.longitude
                )
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
                                currFireEntity.latitude,
                                currFireEntity.longitude
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
        MapEffect(key1 = countryLatLng, key2 = moveToFireCountry) {
            if (countryLatLng != null && moveToFireCountry) {
                cameraPositionState.animate(
                    update = CameraUpdateFactory.newCameraPosition(
                        CameraPosition(
                            LatLng(
                                countryLatLng.latitude,
                                countryLatLng.longitude
                            ),
                            1.0f,
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
            currFireEntity = currFireEntity
        )
    }
}

@OptIn(MapsComposeExperimentalApi::class)
@Composable
fun CustomRendererClustering(
    items: List<FireItem>,
    modifier: Modifier = Modifier,
    colorClusterContent: (Collection<FireItem>) -> Color,
    onClusterItemClick: (FireItem) -> Unit,
    currFireEntity: FireEntity?
) {
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val screenHeight = configuration.screenHeightDp.dp
    val screenWidth = configuration.screenWidthDp.dp
    val clusterManager = rememberClusterManager<FireItem>()

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
            com.kyungsuksong.doomsday.compose.fire.ClusterItemContent(
                modifier = modifier.size(25.dp),
                color = pointBetweenColors(it.fireEntity.confidence/10.0),
                // selected = currFireEntity?.id.equals(it.fireEntity.id)
            )
        },
        clusterManager = clusterManager,
    )
    SideEffect {
        clusterManager ?: return@SideEffect
        clusterManager.setOnClusterClickListener {
            Log.d(FIRE_MAP_CLUSTERING_TAG, "Cluster clicked! $it")
            false
        }
        clusterManager.setOnClusterItemClickListener {
            onClusterItemClick(it)
            false// true disables cluster item click
        }
        clusterManager.setOnClusterItemInfoWindowClickListener {
            Log.d(FIRE_MAP_CLUSTERING_TAG, "Cluster item info window clicked! $it")
        }
        clusterManager.markerCollection.setInfoWindowAdapter(object : GoogleMap.InfoWindowAdapter {
            @SuppressLint("InflateParams")
            var mWindow: View = LayoutInflater
                .from(context)
                .inflate(R.layout.fire_info_window, null)

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
    LaunchedEffect(currFireEntity) {
        clusterManager ?: return@LaunchedEffect
        clusterManager.markerCollection.markers.forEach {
            if (currFireEntity?.latitude?.equals(it.position.latitude) == true &&
                currFireEntity.longitude.equals(it.position.longitude)) {
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