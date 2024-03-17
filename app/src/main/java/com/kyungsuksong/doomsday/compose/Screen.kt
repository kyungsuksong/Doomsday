package com.kyungsuksong.doomsday.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class Screen(
    val route: String,
    val navArgument: List<NamedNavArgument> = emptyList()
) {
    data object Home: Screen(route = "home")

    data object Detail: Screen(
        route = "asteroid_detail",
        navArgument = listOf(
            navArgument(DETAIL_SCREEN_NAV_ARG) {
                type = NavType.LongType
            }
        )
    ) {
        fun createRoute(asteroidId: Long) = route + "/${asteroidId}"
    }

    companion object {
        const val DETAIL_SCREEN_NAV_ARG = "asteroidId"
    }
}