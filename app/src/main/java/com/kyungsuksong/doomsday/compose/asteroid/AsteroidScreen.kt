package com.kyungsuksong.doomsday.compose.asteroid

import android.app.Activity
import android.content.Context
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.work.WorkInfo
import com.kyungsuksong.doomsday.R
import com.kyungsuksong.doomsday.data.room.AsteroidEntity
import com.kyungsuksong.doomsday.data.room.PicOfDayEntity
import com.kyungsuksong.doomsday.util.Dimens
import com.kyungsuksong.doomsday.util.FIRST_LAUNCH_ASTEROID
import com.kyungsuksong.doomsday.util.SHARED_PREFERENCE_NAME
import com.kyungsuksong.doomsday.util.getAsteroidDaysString
import com.kyungsuksong.doomsday.viewmodel.AsteroidViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder

@Composable
fun AsteroidScreen(
    modifier: Modifier = Modifier,
    asteroidViewModel: AsteroidViewModel = hiltViewModel<AsteroidViewModel>(),
    isNetworkConnected: Boolean,
    onAsteroidItemClick: (AsteroidEntity) -> Unit
) {
    var picOfDay: PicOfDayEntity? by remember { mutableStateOf(null) }
    var asteroids: List<AsteroidEntity> by remember { mutableStateOf(emptyList()) }

    val activity = LocalContext.current as Activity
    val preferences = activity.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    val isFirstLaunch = preferences.getBoolean(FIRST_LAUNCH_ASTEROID, true)

    var showLoadingProgress: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        if (isFirstLaunch && isNetworkConnected) {
            asteroidViewModel.startWorker()
            preferences.edit().putBoolean(FIRST_LAUNCH_ASTEROID, false).apply()
        }
    }

    LaunchedEffect(Unit) {
        asteroidViewModel.filteredListOfAsteroid.collect {
            if (it.isNotEmpty()) asteroids = it
        }
    }

    LaunchedEffect(Unit) {
        asteroidViewModel.picOfDay.collect {
            if (it != null) picOfDay = it
        }
    }

    LaunchedEffect(Unit) {
        asteroidViewModel.workState.collect { list ->
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
            Column(
                modifier = modifier
            ) {
                // pic of day
                PicOfDayImage(
                    modifier = modifier,
                    picOfDay = picOfDay
                )

                // label, dropdown menu button, lazy list
                AsteroidLazyList(
                    modifier = modifier,
                    onDropdownMenuClick = {
                        asteroidViewModel.updateAsteroidType(it)
                    },
                    asteroids = asteroids,
                    onAsteroidItemClick = {
                        onAsteroidItemClick(it)
                    }
                )
            }

            if (showLoadingProgress) {
                CircularProgressIndicator(
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
fun PicOfDayImage(
    modifier: Modifier = Modifier,
    picOfDay: PicOfDayEntity? = null
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.Black,
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        AsteroidImage(
            imageUrl = picOfDay?.url ?: "",
            imageHeight = Dimens.imageOfTheDayHeight,
            modifier = modifier
        )

        Row(
            modifier = modifier
                .wrapContentWidth()
                .padding(
                    start = Dimens.PaddingExtraSmall,
                    top = Dimens.PaddingExtraSmall
                )
                .align(Alignment.TopStart)
        ) {
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
            Balloon(
                modifier = modifier,
                builder = balloonBuilder,
                balloonContent = {
                    Text(text = picOfDay?.title ?: "Title not available")
                }
            ) { balloonWindow ->
                TextButton(
                    onClick = { balloonWindow.showAlignTop() },
                    modifier = modifier
                        .wrapContentSize()
                        .width(IntrinsicSize.Min)
                        .height(IntrinsicSize.Min)
                ) {
                    Text(
                        text = stringResource(id = R.string.pic_of_day),
                        fontWeight = FontWeight.Bold,
                        modifier = modifier
                            .wrapContentSize()
                            .width(IntrinsicSize.Max)
                            .height(IntrinsicSize.Min)
                            .background(Color.White),
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@Composable
fun AsteroidLazyList(
    modifier: Modifier = Modifier,
    onDropdownMenuClick: (String) -> Unit,
    asteroids: List<AsteroidEntity>,
    onAsteroidItemClick: (AsteroidEntity) -> Unit
) {
    Column(
        modifier = modifier
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
                    text = stringResource(id = R.string.label_asteroid),
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
                        Text(text = stringResource(id = R.string.label_asteroid_tooltip_info))
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

            AsteroidTypeDropdownMenu(
                modifier = modifier
                    .padding(end = Dimens.PaddingMedium),
                onClick = {
                    onDropdownMenuClick(it)
                }
            )
        }

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
                items = asteroids,
                itemContent = {
                    AsteroidLazyListItem(
                        modifier = modifier,
                        asteroidEntity = it,
                        onListItemClick = {
                            onAsteroidItemClick(it)
                        }
                    )
                }
            )
        }
    }
}

@Composable
fun AsteroidLazyListItem(
    modifier: Modifier = Modifier,
    asteroidEntity: AsteroidEntity,
    onListItemClick: () -> Unit
) {
    val cardSideMargin = dimensionResource(id = R.dimen.card_horiz_margin)
    val cardTextMargin = dimensionResource(id = R.dimen.card_text_margin)

    ElevatedCard(
        onClick = onListItemClick,
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
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = modifier.padding(cardTextMargin)
            ) {
                Text(
                    text = asteroidEntity.codename,
                    modifier = modifier.wrapContentWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = getAsteroidDaysString(asteroidEntity),
                    modifier = modifier.wrapContentWidth(),
                    style = MaterialTheme.typography.titleMedium
                )
            }

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
            Balloon(
                modifier = modifier.padding(end = Dimens.PaddingMedium),
                builder = balloonBuilder,
                balloonContent = {
                    Text(text = when(asteroidEntity.isPotentiallyHazardous) {
                        true -> "This is a potentially hazardous asteroid!!"
                        false -> "This is a safe asteroid"
                    })
                }
            ) { balloonWindow ->
                IconButton(
                    onClick = { balloonWindow.showAlignTop() },
                    modifier = modifier
                ) {
                    Icon(
                        painter = when(asteroidEntity.isPotentiallyHazardous) {
                            true -> painterResource(id = R.drawable.ic_status_potentially_hazardous)
                            false -> painterResource(id = R.drawable.ic_status_normal)
                        },
                        tint = when(asteroidEntity.isPotentiallyHazardous) {
                            true -> colorResource(id = R.color.potentially_hazardous)
                            false -> colorResource(id = R.color.normal)
                        },
                        modifier = modifier,
                        contentDescription = null,
                    )
                }
            }
        }
    }
}