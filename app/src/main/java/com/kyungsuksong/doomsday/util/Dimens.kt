package com.kyungsuksong.doomsday.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import com.kyungsuksong.doomsday.R

object Dimens {
    val imageOfTheDayHeight: Dp
        @Composable get() = dimensionResource(R.dimen.image_of_the_day_height)

    val PaddingMedium: Dp
        @Composable get() = dimensionResource(R.dimen.padding_medium)

    val PaddingSmall: Dp
        @Composable get() = dimensionResource(R.dimen.padding_small)

    val PaddingExtraSmall: Dp
        @Composable get() = dimensionResource(R.dimen.padding_extra_small)

    val spacerWidthLarge: Dp
        @Composable get() = dimensionResource(id = R.dimen.spacer_width_large)

    val mapHeight: Dp
        @Composable get() = dimensionResource(R.dimen.map_height)

    val asteroidDetailScreenImageHeight: Dp
        @Composable get() = dimensionResource(R.dimen.asteroid_detail_screen_image_height)
}