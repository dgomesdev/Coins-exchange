package com.dgomesdev.exchangeapp.presentation.ui.composables

import android.widget.Toast
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.presentation.screens.OnCoinConversion
import com.dgomesdev.exchangeapp.presentation.ui.theme.barColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeApp(
    convertedValues: List<Double>,
    todayValues: List<Double>,
    lastUpdateDate: String,
    onCoinConversion: OnCoinConversion,
    statusMessage: String,
) {
    val navController = rememberNavController()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        topBar = { ExchangeTopBar() },
        bottomBar = { ExchangeBottomBar { navController.navigate(it) } },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) {
        ExchangeNavHost(
            navController = navController,
            modifier = Modifier.padding(it),
            convertedValues = convertedValues,
            todayValues = todayValues,
            lastUpdateDate = lastUpdateDate,
            onCoinConversion = onCoinConversion,
            onConversionButtonClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        statusMessage,
                        duration = SnackbarDuration.Short
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExchangeTopBar() {
    val context = LocalContext.current
    TopAppBar(
        title = { Text(stringResource(R.string.app_name), color = Color.Black) },
        actions = {
            IconButton(onClick = {
                Toast.makeText(
                    context,
                    "Developed by Dgomes Dev",
                    Toast.LENGTH_SHORT
                ).show()
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
        mutableStateOf(0)
    }
    val screens = listOf("Conversion screen", "Today's values screen")
    NavigationBar(
        containerColor = barColor
    ) {
        screens.forEachIndexed { index, screen ->
            NavigationBarItem(
                selected = currentScreen == index,
                onClick = { navigateToScreen(screen); currentScreen = index },
                icon = {
                    if (index == 0) Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = "Main screen",
                        tint = Color.Black
                    )
                    else Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Today's conversion values",
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