package me.aleksandarzekovic.labkmaspacex.android.ui.characters

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Divider
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import me.aleksandarzekovic.labkmaspacex.fragment.ShipsDetail
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.filterIsInstance
import me.aleksandarzekovic.labkmaspacex.android.R
import me.aleksandarzekovic.labkmaspacex.android.designsystem.component.SpaceXTopAppBar
import me.aleksandarzekovic.labkmaspacex.android.theme.SpaceXTheme
import me.aleksandarzekovic.labkmaspacex.shared.ships.ShipsSideEffect
import me.aleksandarzekovic.labkmaspacex.shared.ships.ShipsState
import me.aleksandarzekovic.labkmaspacex.shared.ships.ShipsStore
import org.koin.java.KoinJavaComponent.inject


@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ShipsScreen(
    onShipClicked: (id: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val store: ShipsStore by inject(ShipsStore::class.java)

    val storeState = store.observeState().collectAsStateWithLifecycle()
    val error = store.observeSideEffect()
        .filterIsInstance<ShipsSideEffect.Error>()
        .collectAsStateWithLifecycle(null)

    val snackbarHostState = remember { SnackbarHostState() }

    val scaffoldState = rememberScaffoldState()

    LaunchedEffect(error.value) {
        error.value?.let {
            scaffoldState.snackbarHostState.showSnackbar(
                message = it.error.message.toString(),
                duration = SnackbarDuration.Short,
            )
        }
    }

    Scaffold(
        topBar = { SpaceXTopAppBar(titleRes = R.string.ships) },
        contentColor = MaterialTheme.colors.onBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        scaffoldState = scaffoldState
    ) { padding ->
        Row(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                ),
        ) {
            ShipsScreenContent(
                feedState = storeState.value,
                onShipClicked = onShipClicked,
                modifier = modifier
            )
        }
    }
}

@Composable
private fun ShipsScreenContent(
    modifier: Modifier,
    feedState: ShipsState,
    onShipClicked: (id: String) -> Unit
) {

    val listState = rememberLazyListState()

    when {
        feedState.progress -> {
            Box(
                modifier = modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        feedState.ships.isNotEmpty() -> {
            LazyColumn(
                modifier = modifier
                    .fillMaxSize(),
                state = listState
            ) {
                items(feedState.ships) {
                    ShipItem(shipDetail = it, onShipClicked = onShipClicked)
                }
            }
        }
    }

}


@Composable
fun ShipItem(
    shipDetail: ShipsDetail,
    onShipClicked: (id: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = {
                shipDetail.id?.let { onShipClicked(it) }
            })
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        AsyncImage(
            model = shipDetail.image ?: R.drawable.ic_broken_image,
            contentDescription = shipDetail.name,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .size(60.dp)
                .clip(RectangleShape)
        )

        Column(modifier = Modifier.padding(start = 8.dp, end = 8.dp)) {
            Text(
                shipDetail.name.orEmpty(),
                style = MaterialTheme.typography.h6,
                fontWeight = FontWeight.Bold
            )
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    shipDetail.home_port.orEmpty(),
                    style = MaterialTheme.typography.body2
                )
            }
        }
    }
    Divider()
}


@Preview
@Composable
fun ShipItemPreview() {
    SpaceXTheme {
        ShipItem(
            shipDetail = ShipsDetail(
                id = "",
                name = "American Islander",
                type = "Cargo",
                home_port = "Port of Los Angeles",
                image = "https://i.imgur.com/jmj8Sh2.jpg"
            ),
            onShipClicked = {}
        )
    }
}