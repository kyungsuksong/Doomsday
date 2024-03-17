package com.kyungsuksong.doomsday.compose.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kyungsuksong.doomsday.compose.asteroid.AsteroidConnectivityStatus
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeConnectivityStatus
import com.kyungsuksong.doomsday.compose.fire.FireConnectivityStatus
import com.kyungsuksong.doomsday.data.room.AsteroidEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomePagerScreen(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    pages: Array<HomePage>,
    onAsteroidItemClick: (AsteroidEntity) -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val coroutineScope = rememberCoroutineScope()

        TabRow(
            selectedTabIndex = pagerState.currentPage
        ) {
            pages.forEachIndexed { idx, page ->
                val title = stringResource(id = page.titleResId)
                Tab(
                    selected = pagerState.currentPage == idx,
                    onClick = {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(idx)
                        }
                    },
                    text = {
                        Text(text = title)
                    },
                    icon = {
                        Icon(
                            painter = painterResource(id = page.drawableResId),
                            contentDescription = title
                        )
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.secondary
                )
            }
        }

        HorizontalPager(
            modifier = modifier.background(MaterialTheme.colorScheme.background),
            state = pagerState,
            verticalAlignment = Alignment.Top,
            userScrollEnabled = false
        ) { idx ->
            when(pages[idx]) {
                HomePage.ASTEROID -> {
                    AsteroidConnectivityStatus(
                        modifier = modifier,
                        onAsteroidItemClick = {
                            onAsteroidItemClick(it)
                        }
                    )
                }

                HomePage.EARTHQUAKE -> {
                    EarthquakeConnectivityStatus(
                        modifier = modifier
                    )
                }

                HomePage.FIRE -> {
                    FireConnectivityStatus(
                        modifier = modifier
                    )
                }
            }
        }
    }
}