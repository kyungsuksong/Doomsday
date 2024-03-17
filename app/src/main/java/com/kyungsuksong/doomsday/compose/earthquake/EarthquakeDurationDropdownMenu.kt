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
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeDurationDropdownItem.DAYS_15
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeDurationDropdownItem.DAYS_3
import com.kyungsuksong.doomsday.compose.earthquake.EarthquakeDurationDropdownItem.WEEK

object EarthquakeDurationDropdownItem {
    const val DAYS_15 = "15 Days"
    const val WEEK = "Week"
    const val DAYS_3 = "3 Days"
}

@Composable
fun EarthquakeDurationDropdownMenu(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    var expanded by remember{ mutableStateOf(false) }

    val items = listOf(DAYS_15, WEEK, DAYS_3)

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