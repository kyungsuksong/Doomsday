package com.kyungsuksong.doomsday.compose.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.kyungsuksong.doomsday.data.room.AsteroidEntity

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onAsteroidItemClick: (AsteroidEntity) -> Unit,
    pages: Array<HomePage> = HomePage.entries.toTypedArray(),
) {
    val pagerState = rememberPagerState(pageCount = {pages.size})

    HomePagerScreen(
        modifier = modifier,
        pagerState = pagerState,
        pages = pages,
        onAsteroidItemClick = onAsteroidItemClick
    )
}



