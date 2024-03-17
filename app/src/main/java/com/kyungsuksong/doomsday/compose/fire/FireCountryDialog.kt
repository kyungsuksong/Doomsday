package com.kyungsuksong.doomsday.compose.fire

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import com.kyungsuksong.doomsday.R
import com.kyungsuksong.doomsday.util.Dimens
import com.kyungsuksong.doomsday.util.FireCountry.fireCountryMap
import java.util.Locale

@Composable
fun FireCountryDialog(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (String) -> Unit
) {
    val properties = DialogProperties(
        dismissOnBackPress = true,
        dismissOnClickOutside = true,
        securePolicy = SecureFlagPolicy.Inherit,
        usePlatformDefaultWidth = true,
        decorFitsSystemWindows = true
    )
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = properties,
    ) {
        DialogContent(
            modifier = modifier,
            onDismissRequest = onDismissRequest,
            onConfirmRequest = onConfirmRequest
        )
    }
}

@Composable
fun DialogContent(
    modifier: Modifier = Modifier,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (String) -> Unit
) {
    val searchText = rememberSaveable { mutableStateOf("") }
    val possibleCountries = remember { mutableStateOf(fireCountryMap.keys.toList()) }
    var filteredCountries: List<String> = emptyList()

    Surface(
        modifier = modifier.wrapContentHeight()
    ) {
        Column(
            modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = modifier,
                text = stringResource(id = R.string.fire_country_dialog_title)
            )
            OutlinedTextField(
                modifier = modifier.fillMaxWidth(),
                value = searchText.value,
                onValueChange = {
                    searchText.value = it
                },
                trailingIcon = {
                    Icon(
                        Icons.Default.Clear,
                        contentDescription = "clear text",
                        modifier = modifier
                            .clickable {
                                searchText.value = ""
                            }
                    )
                }
            )
            LazyColumn(
                modifier = modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .height(dimensionResource(id = R.dimen.fire_country_dialog_lazylist_height))
            ) {
                filteredCountries = if (searchText.value.isEmpty()) {
                    possibleCountries.value // return all countries
                } else {
                    val resultList = ArrayList<String>()
                    for (country in possibleCountries.value) {
                        if (country.lowercase(Locale.getDefault())
                                .contains(searchText.value.lowercase(Locale.getDefault()))
                        ) {
                            resultList.add(country)
                        }
                    }
                    resultList
                }
                items(filteredCountries, itemContent = { item ->
                    DialogLazyListItem(
                        modifier = modifier,
                        item = item,
                        onListItemClick = {
                            //TODO: select
                            searchText.value = item
                        }
                    )
                })
            }
            Row(
                modifier = modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                TextButton(
                    onClick = onDismissRequest
                ) {
                    Text(
                        modifier = modifier,
                        text = stringResource(id = R.string.fire_dialog_cancel_button_text)
                    )
                }
                TextButton(
                    onClick = {
                        //TODO: handle OutOfIndexException here when no country is selected
                        if (filteredCountries.isNotEmpty()) {
                            filteredCountries.forEach {
                                if (it.equals(searchText.value)) {
                                    onConfirmRequest(searchText.value)
                                }
                            }
                        }
                    }
                ) {
                    Text(
                        modifier = modifier,
                        text = stringResource(id = R.string.fire_dialog_confirm_button_text)
                    )
                }
            }
        }
    }
}

@Composable
fun DialogLazyListItem(
    modifier: Modifier = Modifier,
    item: String,
    onListItemClick: () -> Unit
) {
    val cardSideMargin = dimensionResource(id = R.dimen.card_horiz_margin)
    val cardTextMargin = dimensionResource(id = R.dimen.card_text_margin)

    ElevatedCard(
        onClick = onListItemClick,
        modifier = modifier
            .padding(
                horizontal = cardSideMargin,
                vertical = Dimens.PaddingExtraSmall
            )
            .clip(RoundedCornerShape(32.dp)),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = modifier.padding(cardTextMargin),
                text = item
            )
        }
    }
}