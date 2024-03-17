package com.kyungsuksong.doomsday.compose.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.kyungsuksong.doomsday.R

@Composable
fun NoConnectivityAlertDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(Icons.Rounded.Warning, contentDescription = "Example Icon")
        },
        title = {
            Text(text = stringResource(id = R.string.no_connectivity_title))
        },
        text = {
            Text(text = stringResource(id = R.string.no_connectivity_text))
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirmation() }
            ) {
                Text(stringResource(id = R.string.no_connectivity_confirm_button_text))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(id = R.string.no_connectivity_dismiss_button_text))
            }
        }
    )
}