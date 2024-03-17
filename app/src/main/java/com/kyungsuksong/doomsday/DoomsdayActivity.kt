package com.kyungsuksong.doomsday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.kyungsuksong.doomsday.compose.DoomsdayApp
import com.kyungsuksong.doomsday.ui.theme.DoomsdayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DoomsdayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // enableEdgeToEdge() // display edge to edge

        setContent {
            DoomsdayTheme {
                // A surface container using the 'background' color from the theme
                DoomsdayApp()
            }
        }
    }

}