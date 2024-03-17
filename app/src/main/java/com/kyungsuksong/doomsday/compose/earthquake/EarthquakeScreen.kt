package com.kyungsuksong.doomsday.compose.earthquake

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import com.google.android.gms.maps.model.LatLng
import com.kyungsuksong.doomsday.R
import com.kyungsuksong.doomsday.data.room.EarthquakeEntity
import com.kyungsuksong.doomsday.util.Dimens
import com.kyungsuksong.doomsday.util.FIRST_LAUNCH_EARTHQUAKE
import com.kyungsuksong.doomsday.util.SHARED_PREFERENCE_NAME
import com.kyungsuksong.doomsday.util.convertEpochTimeToFormattedDate
import com.kyungsuksong.doomsday.util.pointBetweenColors
import com.kyungsuksong.doomsday.viewmodel.EarthquakeViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder

@Composable
fun EarthquakeScreen(
    modifier: Modifier = Modifier,
    earthquakeViewModel: EarthquakeViewModel = hiltViewModel<EarthquakeViewModel>(),
    isNetworkConnected: Boolean,
) {
    var earthquakeEntities: List<EarthquakeEntity> by remember { mutableStateOf(emptyList()) }
    var earthquakesItems: List<EarthquakeItem> by remember { mutableStateOf(emptyList()) }
    var currEarthquakeEntity: EarthquakeEntity? by remember { mutableStateOf(null) }
    var prevEarthquakeEntity: EarthquakeEntity? by remember { mutableStateOf(null) }

    val activity = LocalContext.current as Activity
    val preferences = activity.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    val isFirstLaunch = preferences.getBoolean(FIRST_LAUNCH_EARTHQUAKE, true)

    var showLoadingProgress: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isFirstLaunch && isNetworkConnected) {
            earthquakeViewModel.startWorker()
            preferences.edit().putBoolean(FIRST_LAUNCH_EARTHQUAKE, false).apply()
        }
    }

    LaunchedEffect(Unit) {
        earthquakeViewModel.listOfEarthquakes.collect { list ->
            earthquakeEntities = list
            earthquakesItems = list.map {
                EarthquakeItem(it)
            }
            // don't select earthquake until user actually selects it
            // currEarthquakeEntity = it[0]
        }
    }

    LaunchedEffect(Unit) {
        earthquakeViewModel.currEarthquake.collect {
            currEarthquakeEntity = it
        }
    }

    LaunchedEffect(Unit) {
        earthquakeViewModel.prevEarthquake.collect {
            prevEarthquakeEntity = it
        }
    }

    LaunchedEffect(Unit) {
        earthquakeViewModel.workState.collect { list ->
            list.forEach { workInfo ->
                showLoadingProgress = when (workInfo.state) {
                    WorkInfo.State.RUNNING -> true
                    else -> false
                }
            }
        }
    }

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            EarthquakeMapClustering(
                itemList = earthquakesItems,
                modifier = modifier,
                currEarthquakeEntity = currEarthquakeEntity,
                prevEarthquakeEntity = prevEarthquakeEntity,
                colorClusterContent = {
                    var maxMag: Double = 0.0
                    it.forEach {
                        maxMag = Math.max(maxMag, it.earthquakeEntity.mag)
                    }
                    pointBetweenColors((Math.min(maxMag, 10.0))/10.0)
                },
                onClusterItemClick = {
                    earthquakeViewModel.updateCurrEarthquake(null)
                    // earthquakeViewModel.updateCurrEarthquake(it.earthquakeEntity)
                }
            )

            if (showLoadingProgress) {
                CircularProgressIndicator(
                    modifier = modifier
                )
            }
        }

        Column(
            modifier = modifier.fillMaxWidth()
        ) {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = modifier.width(Dimens.spacerWidthLarge))

                Row(
                    modifier = modifier.wrapContentWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // create and remember a builder of Balloon.
                    val balloonBuilder = rememberBalloonBuilder {
                        setArrowSize(10)
                        setArrowPosition(0.5f)
                        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                        setWidth(BalloonSizeSpec.WRAP)
                        setHeight(BalloonSizeSpec.WRAP)
                        setPadding(12)
                        setMarginHorizontal(12)
                        setCornerRadius(8f)
                        setBackgroundColorResource(R.color.sky_blue)
                        setBalloonAnimation(BalloonAnimation.ELASTIC)
                    }

                    Text(
                        text = stringResource(id = R.string.label_earthquake),
                        modifier = modifier
                            .wrapContentWidth()
                            .width(IntrinsicSize.Max)
                            .padding(start = Dimens.PaddingMedium),
                        fontWeight = FontWeight.SemiBold
                    )

                    Balloon(
                        modifier = modifier.padding(end = Dimens.PaddingMedium),
                        builder = balloonBuilder,
                        balloonContent = {
                            Text(text = stringResource(id = R.string.label_earthquake_tooltip_info))
                        }
                    ) { balloonWindow ->
                        IconButton(
                            onClick = { balloonWindow.showAlignTop() },
                            modifier = modifier
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_question_mark_medium),
                                tint = colorResource(id = R.color.normal),
                                modifier = modifier,
                                contentDescription = null,
                            )
                        }
                    }
                }

                Row(
                    modifier = modifier
                ) {
                    EarthquakeMagnitudeDropdownMenu(
                        modifier = modifier,
                        onClick = {
                            earthquakeViewModel.updateEarthquakeMagType(it)
                        }
                    )

                    EarthquakeDurationDropdownMenu(
                        modifier = modifier,
                        onClick = {
                            earthquakeViewModel.updateEarthquakeDurationType(it)
                        }
                    )
                }
            }

            if (earthquakeEntities.isNotEmpty()) {
                // recycler view
                LazyColumn(
                    modifier = modifier
                        .border(
                            width = 1.dp,
                            color = Color.Black,
                            shape = RectangleShape
                        )
                ) {
                    items(
                        items = earthquakeEntities,
                        itemContent = {
                            EarthquakeLazyListItem(
                                modifier = modifier,
                                earthquakeEntity = it,
                                onListItemClick = {
                                    earthquakeViewModel.updateCurrEarthquake(it)
                                },
                                isListItemSelected = currEarthquakeEntity?.id == it.id
                            )
                        }
                    )
                }
            } else {
                Column(
                    modifier = modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = stringResource(id = R.string.no_earthquake),
                        modifier = modifier
                            .wrapContentWidth()
                            .width(IntrinsicSize.Max)
                            .padding(top = Dimens.PaddingMedium),
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}


@Composable
fun EarthquakeLazyListItem(
    modifier: Modifier = Modifier,
    earthquakeEntity: EarthquakeEntity,
    onListItemClick: () -> Unit,
    isListItemSelected: Boolean
) {
    val cardSideMargin = dimensionResource(id = R.dimen.card_horiz_margin)
    val cardTextMargin = dimensionResource(id = R.dimen.card_text_margin)

    ElevatedCard(
        onClick = {
            onListItemClick()
        },
        modifier = modifier
            .padding(
                horizontal = cardSideMargin,
                vertical = Dimens.PaddingExtraSmall
            )
            .clip(RoundedCornerShape(32.dp))
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(32.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Column(
            modifier = modifier
                .padding(cardTextMargin)
                .fillMaxWidth()
        ) {
            Text(
                text = earthquakeEntity.place,
                modifier = modifier
                    .wrapContentSize(),
                style = MaterialTheme.typography.titleLarge
            )
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = modifier
                ) {
                    Text(
                        text = "magnitude: %.2f".format(earthquakeEntity.mag),
                        modifier = modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Time: " + convertEpochTimeToFormattedDate(earthquakeEntity.time) + " UTC",
                        modifier = modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.baseline_circle_24),
                    modifier = modifier,
                    contentDescription = null,
                    tint = pointBetweenColors(Math.min(earthquakeEntity.mag, 10.0)/10.0)
                )
            }
        }
    }
}