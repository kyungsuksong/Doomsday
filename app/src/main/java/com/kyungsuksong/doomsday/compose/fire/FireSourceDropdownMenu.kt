package com.kyungsuksong.doomsday.compose.fire

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

object FireSourceDropdownItem {
    const val ALL = "All"
    const val VIIRS_SNPP_NRT = "VIIRS SNPP NRT"
    const val VIIRS_NOAA20_NRT = "VIIRS NOAA20 NRT"
    const val VIIRS_NOAA21_NRT = "VIIRS NOAA21 NRT"
}

@Composable
fun FireSourceDropdownMenu(
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit,
    enabled: Boolean
) {
    var expanded by remember{ mutableStateOf(false) }

    val items = listOf(
        FireSourceDropdownItem.ALL,
        FireSourceDropdownItem.VIIRS_SNPP_NRT,
        FireSourceDropdownItem.VIIRS_NOAA20_NRT,
        FireSourceDropdownItem.VIIRS_NOAA21_NRT
    )

    var selectedIndex by remember{ mutableIntStateOf(0) }

    Box(
        modifier = modifier
    ) {
        IconButton(
            onClick = {
                expanded = !expanded
            },
            enabled = enabled
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