package com.kyungsuksong.doomsday.compose

import android.app.Activity
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ShareCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kyungsuksong.doomsday.R
import com.kyungsuksong.doomsday.compose.asteroidDetail.AsteroidDetailScreen
import com.kyungsuksong.doomsday.compose.home.HomeScreen
import com.kyungsuksong.doomsday.data.room.AsteroidEntity

@Composable
fun DoomsdayApp() {
    val navHostController = rememberNavController()
    AsteroidNavHost(navHostController)
}

@Composable
fun AsteroidNavHost(navHostController: NavHostController) {
    val activity = LocalContext.current as Activity

    NavHost(navController = navHostController, startDestination = Screen.Home.route) {
        // home
        composable(route = Screen.Home.route) {
            HomeScreen(
                onAsteroidItemClick = {
                    navHostController.navigate(
                        Screen.Detail.createRoute(it.id)
                    )
                }
            )
        }

        // detail
        composable(
            route = Screen.Detail.route + "/{asteroidId}",
            arguments = Screen.Detail.navArgument
        ) {
            AsteroidDetailScreen(
                onBackClick = { navHostController.navigateUp() },
                onShareClick = {
                    createShareIntent(activity, it)
                }
            )
        }
    }
}

private fun createShareIntent(
    activity: Activity,
    asteroidEntity: AsteroidEntity
) {
    val shareText = activity.getString(R.string.share_text_asteroid, asteroidEntity.codename)
    val shareIntent = ShareCompat.IntentBuilder(activity)
        .setText(shareText)
        .setType("text/plain")
        .createChooserIntent()
        .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
    activity.startActivity(shareIntent)
}