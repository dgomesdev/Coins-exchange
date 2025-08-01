package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dgomesdev.exchangeapp.presentation.screens.AllValuesScreen
import com.dgomesdev.exchangeapp.presentation.screens.ConversionScreen
import com.dgomesdev.exchangeapp.presentation.ui.Route
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeViewModel

@Composable
fun ExchangeNavHost(
    navController: NavHostController,
    modifier: Modifier,
    viewModel: ExchangeViewModel,
    snackbarHostState: SnackbarHostState
) {
    val padding = Modifier.padding(8.dp)
    NavHost(
        navController = navController,
        startDestination = Route.CONVERSION_SCREEN.route,
        modifier = modifier
    ) {
        composable(
            route = Route.CONVERSION_SCREEN.route
        ) {
            ConversionScreen(
                modifier = padding,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState
            )
        }
        composable(
            route = Route.ALL_VALUES_SCREEN.route
        ) {
            AllValuesScreen(
                modifier = padding,
                viewModel = viewModel,
                snackbarHostState = snackbarHostState
            )
        }
    }
}