package me.aleksandarzekovic.labkmaspacex.android.ui.shipdetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.filterIsInstance
import me.aleksandarzekovic.labkmaspacex.android.R
import me.aleksandarzekovic.labkmaspacex.android.designsystem.component.SpaceXTopAppBar
import me.aleksandarzekovic.labkmaspacex.android.theme.SpaceXTheme
import me.aleksandarzekovic.labkmaspacex.fragment.ShipsDetail
import me.aleksandarzekovic.labkmaspacex.shared.shipdetails.ShipDetailsAction
import me.aleksandarzekovic.labkmaspacex.shared.shipdetails.ShipDetailsState
import me.aleksandarzekovic.labkmaspacex.shared.shipdetails.ShipDetailsStore
import me.aleksandarzekovic.labkmaspacex.shared.ships.ShipsSideEffect
import org.koin.java.KoinJavaComponent

@OptIn(ExperimentalLayoutApi::class)
@Composable
internal fun ShipDetailsScreen(
    modifier: Modifier = Modifier,
    shipId: String,
    popBack: () -> Unit,
) {
    val store: ShipDetailsStore by KoinJavaComponent.inject(ShipDetailsStore::class.java)
    val error = store.observeSideEffect()
        .filterIsInstance<ShipsSideEffect.Error>()
        .collectAsStateWithLifecycle(null)

    val state = store.observeState().collectAsStateWithLifecycle()

    LaunchedEffect(shipId) {
        store.dispatch(ShipDetailsAction.Init(shipId))
    }

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
        topBar = {
            SpaceXTopAppBar(titleRes = R.string.details, onNavigationClick = popBack)
        },
        contentColor = MaterialTheme.colors.onBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        scaffoldState = scaffoldState
    )
    { padding ->
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
            ShipDetailsScreenContent(modifier = modifier, state = state.value)
        }
    }
}

@Composable
fun ShipDetailsScreenContent(
    modifier: Modifier,
    state: ShipDetailsState
) {
    if (state.progress) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

    state.ship?.let { ShipDetailsItem(it) }
}

@Composable
fun ShipDetailsItem(ship: ShipsDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        AsyncImage(
            model = ship.image ?: R.drawable.ic_broken_image,
            contentDescription = ship.name,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RectangleShape)
        )

        ship.name?.let { name ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.name, name))
        }

        ship.home_port?.let { homePort ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.home_port, homePort))
        }

        ship.type?.let { type ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = stringResource(R.string.type, type))
        }
    }
}


@Preview
@Composable
fun ShipDetailsItemPreview() {
    SpaceXTheme {
        ShipDetailsItem(
            ship = ShipsDetail(
                id = "",
                name = "American Islander",
                type = "Cargo",
                home_port = "Port of Los Angeles",
                image = "https://i.imgur.com/jmj8Sh2.jpg"
            )
        )
    }
}