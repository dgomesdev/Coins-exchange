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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import com.dgomesdev.exchangeapp.presentation.ui.Route
import com.dgomesdev.exchangeapp.presentation.ui.theme.barColor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

typealias OnChangeAmount = (String) -> Unit
typealias OnChangeSelectedCoin = (Coin) -> Unit
@Composable
fun ExchangeApp(
    allValues: List<ExchangeModel>,
    lastUpdateDate: String,
    onChangeAmount: OnChangeAmount,
    selectedCoin: Coin,
    onChangeSelectedCoin: OnChangeSelectedCoin,
    amountToBeConverted: Double
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { ExchangeTopBar(scope, snackbarHostState) },
        bottomBar = { ExchangeBottomBar { navController.navigate(it) } },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        ExchangeNavHost(
            navController = navController,
            modifier = Modifier.padding(it),
            allValues = allValues,
            lastUpdateDate = lastUpdateDate,
            onChangeAmount = onChangeAmount,
            selectedCoin = selectedCoin,
            onChangeSelectedCoin = onChangeSelectedCoin,
            amountToBeConverted = amountToBeConverted
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeTopBar(scope: CoroutineScope, snackbarHostState: SnackbarHostState) {
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
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = barColor)
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