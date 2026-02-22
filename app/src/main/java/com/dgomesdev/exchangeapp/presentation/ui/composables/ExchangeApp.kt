package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import androidx.window.core.layout.WindowSizeClass.Companion.HEIGHT_DP_MEDIUM_LOWER_BOUND
import androidx.window.core.layout.WindowSizeClass.Companion.WIDTH_DP_MEDIUM_LOWER_BOUND
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.presentation.screens.LargeScreenLayout
import com.dgomesdev.exchangeapp.presentation.ui.Route
import com.dgomesdev.exchangeapp.presentation.ui.theme.barColor
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeState
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeStateStatus
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeViewModel
import kotlinx.coroutines.launch

@Composable
fun ExchangeApp(
    viewModel: ExchangeViewModel,
    state: ExchangeState
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val retryLabel = stringResource(R.string.retry)
    val navController = rememberNavController()
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val isLargeHeight = windowSizeClass.isHeightAtLeastBreakpoint(HEIGHT_DP_MEDIUM_LOWER_BOUND)
    val isLargeWidth = windowSizeClass.isWidthAtLeastBreakpoint(WIDTH_DP_MEDIUM_LOWER_BOUND)

    val isLargeScreen = isLargeHeight && isLargeWidth
    val isLandscape = !isLargeHeight && isLargeWidth

    LaunchedEffect(state.status) {
        if (state.status == ExchangeStateStatus.ERROR) {
            launch {
                snackbarHostState.showSnackbar(
                    message = state.errorMessage,
                    actionLabel = retryLabel
                ).also {
                    if (it == SnackbarResult.ActionPerformed) {
                        viewModel.getValues()
                    }
                }
            }
        }
    }

    Scaffold(
        topBar = { ExchangeTopBar(snackbarHostState) },
        bottomBar = { if (!isLargeScreen) { ExchangeBottomBar { navController.navigate(it) } } },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        when (state.status) {
            ExchangeStateStatus.LOADING -> LoadingIndicator()
            ExchangeStateStatus.ERROR -> ErrorButton(retryAction = viewModel::getValues)
            ExchangeStateStatus.SUCCESS -> {
                if (isLargeScreen) {
                    LargeScreenLayout(
                        modifier = Modifier.padding(it),
                        viewModel = viewModel,
                        amountToBeConverted = state.amountToBeConverted,
                        selectedCoin = state.selectedCoin,
                        lastUpdatedDate = state.lastUpdatedDate
                    )
                } else {
                    ExchangeNavHost(
                        navController = navController,
                        isLandscape = isLandscape,
                        modifier = Modifier.padding(it),
                        viewModel = viewModel,
                        amountToBeConverted = state.amountToBeConverted,
                        selectedCoin = state.selectedCoin,
                        lastUpdatedDate = state.lastUpdatedDate
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeTopBar(snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text(stringResource(R.string.app_name), color = Color.Black) },
        actions = {
            IconButton(onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        "Developed by Dgomes Dev",
                        duration = SnackbarDuration.Short
                    )
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = "Developed by Dgomes Dev",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = barColor)
    )
}

@Composable
fun ExchangeBottomBar(
    navigateToScreen: (String) -> Unit,
) {
    var currentScreen by rememberSaveable {
        mutableIntStateOf(0)
    }
    val screens = Route.entries
    NavigationBar(
        containerColor = barColor
    ) {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                selected = currentScreen == index,
                onClick = { navigateToScreen(screen.route); currentScreen = index },
                icon = {
                    if (index == 0) Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Main screen",
                        tint = Color.Black
                    )
                    else Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "All conversion values",
                        tint = Color.Black
                    )
                },
                label = {
                    Text(
                        if (index == 0) stringResource(R.string.main_screen)
                        else stringResource(R.string.history),
                        color = Color.Black
                    )
                }
            )
        }
    }
}