package me.aleksandarzekovic.labkmaspacex.android.designsystem.component

import androidx.annotation.StringRes
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview


@Composable
fun SpaceXTopAppBar(
    @StringRes titleRes: Int,
) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleRes)) }
    )
}

@Composable
fun SpaceXTopAppBar(
    @StringRes titleRes: Int,
    onNavigationClick: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = stringResource(id = titleRes)) },
        navigationIcon = {
            IconButton(onClick = onNavigationClick) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colors.onSurface,
                )
            }
        },
    )
}

@Preview("Top App Bar")
@Composable
private fun SpaceXTopAppBarPreview() {
    SpaceXTopAppBar(
        titleRes = android.R.string.untitled,
    )
}


@Preview("Top App Bar")
@Composable
private fun SpaceXTopAppBarWithNavigationPreview() {
    SpaceXTopAppBar(
        titleRes = android.R.string.untitled,
        onNavigationClick = {}
    )
}