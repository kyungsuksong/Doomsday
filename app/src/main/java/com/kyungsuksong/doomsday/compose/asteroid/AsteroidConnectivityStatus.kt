package com.kyungsuksong.doomsday.compose.asteroid

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kyungsuksong.doomsday.compose.connectivity.ConnectionState
import com.kyungsuksong.doomsday.compose.connectivity.connectivityState
import com.kyungsuksong.doomsday.compose.home.NoConnectivityAlertDialog
import com.kyungsuksong.doomsday.data.room.AsteroidEntity
import com.kyungsuksong.doomsday.util.FIRST_LAUNCH_ASTEROID
import com.kyungsuksong.doomsday.util.SHARED_PREFERENCE_NAME

@Composable
fun AsteroidConnectivityStatus(
    modifier: Modifier = Modifier,
    onAsteroidItemClick: (AsteroidEntity) -> Unit
) {
    // This will cause re-composition on every network state change
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    val activity = LocalContext.current as Activity
    val preferences = activity.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    val isFirstLaunch = preferences.getBoolean(FIRST_LAUNCH_ASTEROID, true)

    AsteroidScreen(
        modifier = modifier,
        isNetworkConnected = isConnected,
        onAsteroidItemClick = {
            onAsteroidItemClick(it)
        }
    )

    if (!isConnected && isFirstLaunch) {
        NoConnectivityAlertDialog(
            onDismissRequest = {
                activity.finish()
            },
            onConfirmation = {
                val dialogIntent = Intent(Settings.ACTION_SETTINGS)
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                activity.startActivity(dialogIntent)
            }
        )
    }
}