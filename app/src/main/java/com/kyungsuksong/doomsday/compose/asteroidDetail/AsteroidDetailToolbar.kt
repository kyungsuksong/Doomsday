package com.kyungsuksong.doomsday.compose.asteroidDetail
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.kyungsuksong.doomsday.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsteroidDetailToolbar(
    onBackClick: () -> Unit,
    onShareClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surface),
        title = {
            Text(
                text = stringResource(id = R.string.detail_toolbar_label),
                style = MaterialTheme.typography.titleMedium,
                // As title in TopAppBar has extra inset on the left, need to do this: b/158829169
                modifier = modifier
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = modifier
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            val shareContentDescription = stringResource(R.string.detail_share_asteroid)
            IconButton(
                onClick = onShareClick,
                modifier = modifier
                    // Semantics in parent due to https://issuetracker.google.com/184825850
                    .semantics { contentDescription = shareContentDescription }
            ) {
                Icon(
                    Icons.Filled.Share,
                    contentDescription = null
                )
            }
        }
    )
}