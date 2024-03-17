package com.kyungsuksong.doomsday.compose.earthquake

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeMagnitudeDropdownItem.ALL
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeMagnitudeDropdownItem.GREATER_THAN_8
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeMagnitudeDropdownItem.BETWEEN_7_AND_8
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeMagnitudeDropdownItem.BETWEEN_6_AND_7
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeMagnitudeDropdownItem.BETWEEN_5P5_AND_6
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeMagnitudeDropdownItem.BETWEEN_2P5_AND_5P5
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeMagnitudeDropdownItem.LESS_THAN_2P5

object EarthquakeMagnitudeDropdownItem {
    const val ALL = "All"
    const val GREATER_THAN_8 = "8.0 or greater"
    const val BETWEEN_7_AND_8 = "7.0 to 7.99"
    const val BETWEEN_6_AND_7 = "6.0 to 6.99"
    const val BETWEEN_5P5_AND_6 = "5.5 to 5.99"
    const val BETWEEN_2P5_AND_5P5 = "2.5 to 5.49"
    const val LESS_THAN_2P5 = "Less than 2.5"
}

@Composable
fun EarthquakeMagnitudeDropdownMenu(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    var expanded by remember{ mutableStateOf(false) }

    val items = listOf(
        ALL, GREATER_THAN_8, BETWEEN_7_AND_8, BETWEEN_6_AND_7, BETWEEN_5P5_AND_6, BETWEEN_2P5_AND_5P5, LESS_THAN_2P5
    )

    var selectedIndex by remember{ mutableIntStateOf(0) }

    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                expanded = !expanded
            }
        ) {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier
        ) {
            items.forEachIndexed { index, value ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = items[index]
                        )
                    },
                    onClick = {
                        selectedIndex = index
                        expanded = false
                        onClick(value)
                    },
                    modifier = modifier
                )
            }
        }
    }
}