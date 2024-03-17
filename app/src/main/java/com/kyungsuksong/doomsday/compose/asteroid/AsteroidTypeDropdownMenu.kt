package com.kyungsuksong.doomsday.compose.asteroid

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.graphics.Color
import com.kyungsuksong.doomsday.compose.asteroid.AsteroidTypeDropdownItem.ALL
import com.kyungsuksong.doomsday.compose.asteroid.AsteroidTypeDropdownItem.ONWARD
import com.kyungsuksong.doomsday.compose.asteroid.AsteroidTypeDropdownItem.TODAY
import com.kyungsuksong.doomsday.compose.asteroid.AsteroidTypeDropdownItem.PAST

object AsteroidTypeDropdownItem {
    const val ALL = "all"
    const val ONWARD = "onward"
    const val TODAY = "today"
    const val PAST = "past"
}

@Composable
fun AsteroidTypeDropdownMenu(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    val items = listOf(ONWARD, TODAY, PAST)

    var selectedIndex by remember { mutableIntStateOf(1) }

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