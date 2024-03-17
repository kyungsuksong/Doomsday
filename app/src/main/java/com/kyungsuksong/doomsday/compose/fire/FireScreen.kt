package com.kyungsuksong.doomsday.compose.fire

import android.app.Activity
import android.content.Context
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import com.kyungsuksong.doomsday.data.room.FireEntity
import com.kyungsuksong.doomsday.util.Dimens
import com.kyungsuksong.doomsday.util.FIRST_LAUNCH_EARTHQUAKE
import com.kyungsuksong.doomsday.util.FIRST_LAUNCH_FIRE
import com.kyungsuksong.doomsday.util.FireCountry.fireCountryLatLngMap
import com.kyungsuksong.doomsday.util.SHARED_PREFERENCE_NAME
import com.kyungsuksong.doomsday.util.convertEpochTimeToFormattedDate
import com.kyungsuksong.doomsday.util.pointBetweenColors
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_NOAA20_NRT
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_NOAA21_NRT
import com.kyungsuksong.doomsday.viewmodel.FireSourceType.VIIRS_SNPP_NRT
import com.kyungsuksong.doomsday.viewmodel.FireViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder

@Composable
fun FireScreen(
    modifier: Modifier = Modifier,
    fireViewModel: FireViewModel = hiltViewModel<FireViewModel>(),
    isNetworkConnected: Boolean,
) {
    var fireEntities: List<FireEntity> by remember { mutableStateOf(emptyList()) }
    var fireItems: List<FireItem> by remember { mutableStateOf(emptyList()) }
    var currFireEntity: FireEntity? by remember { mutableStateOf(null) }
    var prevFireEntity: FireEntity? by remember { mutableStateOf(null) }
    var countryLatLng: LatLng? by remember { mutableStateOf(null) }

    var showLoadingProgress: Boolean by remember { mutableStateOf(false) }

    val countryDialogState: FireCountryDialogState by fireViewModel.countryDialogState.collectAsState()

    /* val activity = LocalContext.current as Activity
    val preferences = activity.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    val isFirstLaunch = preferences.getBoolean(FIRST_LAUNCH_FIRE, true)

    // Instead of using FireWorker to fetch the data from Api,
    // call Api whenever fireCountry is changed inside FireViewModel
    LaunchedEffect(Unit) {
        if (isFirstLaunch && isNetworkConnected) {
            fireViewModel.startWorker()
            preferences.edit().putBoolean(FIRST_LAUNCH_FIRE, false).apply()
        }
    } */

    LaunchedEffect(Unit) {
        fireViewModel.listOfFiresInCountry.collect { list ->
            fireEntities = list
            fireItems = list.map { FireItem(it) }
        }
    }

    LaunchedEffect(Unit) {
        fireViewModel.currFire.collect {
            currFireEntity = it
        }
    }
    LaunchedEffect(Unit) {
        fireViewModel.prevFire.collect {
            prevFireEntity = it
        }
    }

    LaunchedEffect(Unit) {
        fireViewModel.fireCountryLatLng.collect {
            countryLatLng = it ?: fireCountryLatLngMap["United States"]
        }
    }

    LaunchedEffect(Unit) {
        fireViewModel.workState.collect { list ->
            list.forEach { workInfo ->
                showLoadingProgress = when (workInfo.state) {
                    WorkInfo.State.RUNNING -> true
                    WorkInfo.State.SUCCEEDED -> {
                        fireViewModel.setFireCountryFetchTime()
                        false
                    }
                    else -> false
                }
            }
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
        ) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                FireMapClustering(
                    itemList = fireItems,
                    modifier = modifier,
                    currFireEntity = currFireEntity,
                    prevFireEntity = prevFireEntity,
                    colorClusterContent = {
                        var maxConfidence: Double = 0.0
                        it.forEach {
                            maxConfidence = Math.max(maxConfidence, it.fireEntity.confidence)
                        }
                        pointBetweenColors(maxConfidence/10.0)
                    },
                    onClusterItemClick = {
                        fireViewModel.updateCurrFire(null)
                        // fireViewModel.updateCurrFire(it.fireEntity)
                    },
                    countryLatLng = countryLatLng
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
                            text = stringResource(id = R.string.label_fire),
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
                                Text(text = stringResource(id = R.string.label_fire_tooltip_info))
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
                        IconButton(
                            modifier = modifier,
                            onClick = {
                                fireViewModel.turnOnCountryDialog()
                            },
                            enabled = !showLoadingProgress
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = null
                            )
                        }

                        FireSourceDropdownMenu(
                            modifier = modifier,
                            onClick = {
                                fireViewModel.updateFireSourceType(it)
                            },
                            enabled = !showLoadingProgress
                        )
                    }
                }

                if (fireEntities.isNotEmpty()) {
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
                            items = fireEntities,
                            itemContent = {
                                FireLazyListItem(
                                    modifier = modifier,
                                    fireEntity = it,
                                    onListItemClick = {
                                        fireViewModel.updateCurrFire(it)
                                    },
                                    isListItemSelected = currFireEntity?.id == it.id
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
                            text = stringResource(id = R.string.no_fire),
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

        if (countryDialogState.visible) {
            FireCountryDialog(
                modifier = modifier,
                onDismissRequest = {
                    fireViewModel.dismissCountryDialog()
                },
                onConfirmRequest = {
                    fireViewModel.updateFireCountry(it)
                    fireViewModel.dismissCountryDialog()
                }
            )
        }
    }
}

@Composable
fun FireLazyListItem(
    modifier: Modifier = Modifier,
    fireEntity: FireEntity,
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
                text = "Source: " + when(fireEntity.sourceType) {
                    VIIRS_SNPP_NRT -> "VIIRS SNPP NRT"
                    VIIRS_NOAA20_NRT -> "VIIRS NOAA20 NRT"
                    VIIRS_NOAA21_NRT -> "VIIRS NOAA21 NRT"
                    else -> "Unknown"
                },
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
                        text = "Brightness (K): %.2f".format(fireEntity.brightness),
                        modifier = modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "Date: " + fireEntity.acquisition_date + ", Time: " + fireEntity.acquisition_time + " UTC",
                        modifier = modifier.wrapContentWidth(),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.baseline_circle_24),
                    modifier = modifier,
                    contentDescription = null,
                    tint = pointBetweenColors(fireEntity.confidence/10.0)
                )
            }
        }
    }
}