package com.kyungsuksong.doomsday.compose.earthquake

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.kyungsuksong.doomsday.compose.connectivity.ConnectionState
import com.kyungsuksong.doomsday.compose.connectivity.connectivityState
import com.kyungsuksong.doomsday.compose.home.NoConnectivityAlertDialog
import com.kyungsuksong.doomsday.util.FIRST_LAUNCH_EARTHQUAKE
import com.kyungsuksong.doomsday.util.SHARED_PREFERENCE_NAME

@Composable
fun EarthquakeConnectivityStatus(
    modifier: Modifier = Modifier,
) {
    // This will cause re-composition on every network state change
    val connection by connectivityState()
    val isConnected = connection === ConnectionState.Available

    val activity = LocalContext.current as Activity
    val preferences = activity.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    val isFirstLaunch = preferences.getBoolean(FIRST_LAUNCH_EARTHQUAKE, true)

    EarthquakeScreen(
        modifier = modifier,
        isNetworkConnected = isConnected,
        // onAsteroidItemClick = { }
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