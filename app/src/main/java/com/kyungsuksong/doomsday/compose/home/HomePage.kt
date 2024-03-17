package com.kyungsuksong.doomsday.compose.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.kyungsuksong.doomsday.R

enum class HomePage(
    @StringRes val titleResId: Int,
    @DrawableRes val drawableResId: Int
) {
    ASTEROID(R.string.asteroid_page_title, R.drawable.ic_asteroid_active),
    EARTHQUAKE(R.string.earthquake_page_title, R.drawable.ic_earthquake_active),
    FIRE(R.string.fire_page_title, R.drawable.ic_fire_active)
}