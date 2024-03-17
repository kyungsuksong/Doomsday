package com.kyungsuksong.doomsday.compose.asteroidDetail

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.hilt.navigation.compose.hiltViewModel
import com.kyungsuksong.doomsday.R
import com.kyungsuksong.doomsday.data.room.AsteroidEntity
import com.kyungsuksong.doomsday.util.Dimens
import com.kyungsuksong.doomsday.util.convertEpochTimeToFormattedDate
import com.kyungsuksong.doomsday.util.getAsteroidDaysString
import com.kyungsuksong.doomsday.viewmodel.DetailViewModel
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.Balloon
import com.skydoves.balloon.compose.rememberBalloonBuilder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AsteroidDetailScreen(
    modifier: Modifier = Modifier,
    detailViewModel: DetailViewModel = hiltViewModel<DetailViewModel>(),
    onBackClick: () -> Unit,
    onShareClick: (AsteroidEntity) -> Unit,
    onAsteroidDetailItemClick: (AsteroidDetailData) -> Unit = {}
) {
    val context = LocalContext.current
    var asteroidEntity: AsteroidEntity? by remember { mutableStateOf(null) }
    var asteroidDetailData: List<AsteroidDetailData> by remember { mutableStateOf(emptyList()) }

    LaunchedEffect(Unit) {
        detailViewModel.asteroid?.collect {
            asteroidEntity = it
            asteroidDetailData = listOf(
                // close approach date
                AsteroidDetailData(
                    title = getString(context, R.string.close_approach_date_label),
                    body = convertEpochTimeToFormattedDate(it.closeApproachDate) +
                            " UTC (" + getAsteroidDaysString(it) + ")",
                    balloonContent = getString(context, R.string.close_approach_date_balloon_content)
                ),
                // absolute magnitude
                AsteroidDetailData(
                    title = getString(context, R.string.absolute_magnitude_label),
                    body = "%.2f".format(it.absoluteMagnitude),
                    balloonContent = getString(context, R.string.absolute_magnitude_balloon_content)
                ),
                // estimated diameter
                AsteroidDetailData(
                    title = getString(context, R.string.estimated_diameter_label),
                    body = "min. %.2f".format(it.estimatedDiameterMin) +
                            " ft - max. %.2f".format(it.estimatedDiameterMax) + " ft",
                    balloonContent = getString(context, R.string.estimated_diameter_balloon_content)
                ),
                // relative velocity
                AsteroidDetailData(
                    title = getString(context, R.string.relative_velocity_label),
                    body = "%.2f".format(it.relativeVelocity) + " miles per hour",
                    balloonContent = getString(context, R.string.relative_velocity_balloon_content)
                ),
                // distance from earth
                AsteroidDetailData(
                    title = getString(context, R.string.distance_from_earth_label),
                    body = "%.2f".format(it.distanceFromEarthInAU) +
                            " AU (%.2f".format(it.distanceFromEarthInMiles) + " miles)",
                    balloonContent = getString(context, R.string.distance_from_earth_balloon_content)
                ),
            )
        }
    }

    if (asteroidEntity != null) {
        Column(
            modifier = modifier
        ) {
            AsteroidDetailToolbar(
                modifier = modifier,
                onBackClick = onBackClick,
                onShareClick = {
                    onShareClick(asteroidEntity!!)
                }
            )

            Image(
                painter = when(asteroidEntity!!.isPotentiallyHazardous) {
                    true -> painterResource(id = R.drawable.asteroid_hazardous)
                    false -> painterResource(id = R.drawable.asteroid_safe)
                },
                contentDescription = null,
                modifier = modifier
                    .fillMaxWidth()
                    .height(Dimens.asteroidDetailScreenImageHeight),
                contentScale = ContentScale.Crop
            )

            // recycler view
            LazyColumn(
                modifier = modifier
                    .fillMaxSize()
                    .border(
                        width = 1.dp,
                        color = Color.Black,
                        shape = RectangleShape
                    )
            ) {
                items(
                    items = asteroidDetailData,
                    itemContent = {
                        AsteroidDetailLazyListItem(
                            modifier = modifier,
                            title = it.title,
                            body = it.body,
                            balloonContent = it.balloonContent,
                            onListItemClick = {
                                onAsteroidDetailItemClick(it)
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun AsteroidDetailLazyListItem(
    modifier: Modifier = Modifier,
    title: String,
    body: String,
    balloonContent: String,
    onListItemClick: () -> Unit
) {
    val cardSideMargin = dimensionResource(id = R.dimen.card_horiz_margin)
    val cardTextMargin = dimensionResource(id = R.dimen.card_text_margin)

    ElevatedCard(
        onClick = onListItemClick,
        modifier = modifier
            .fillMaxWidth()
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
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = modifier
                    .wrapContentWidth()
                    .width(IntrinsicSize.Max)
                    .padding(cardTextMargin)
            ) {
                Text(
                    text = title,
                    modifier = modifier
                        .wrapContentWidth(),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = body,
                    modifier = modifier
                        .wrapContentWidth(),
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
                    Text(text = balloonContent)
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
    }
}