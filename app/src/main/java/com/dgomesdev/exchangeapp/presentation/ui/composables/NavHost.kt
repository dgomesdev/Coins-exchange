package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.presentation.screens.AllValuesScreen
import com.dgomesdev.exchangeapp.presentation.screens.ConversionScreen
import com.dgomesdev.exchangeapp.presentation.ui.Route
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeViewModel

@Composable
fun ExchangeNavHost(
    modifier: Modifier,
    navController: NavHostController,
    isLandscape: Boolean,
    viewModel: ExchangeViewModel,
    amountToBeConverted: String,
    selectedCoin: Coin,
    lastUpdatedDate: String
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
                isLandscape = isLandscape,
                amountToBeConverted = amountToBeConverted,
                selectedCoin = selectedCoin,
                conversionValueTexts = viewModel.getConversionValueTexts(shouldGetAllValues = false),
                onChangeAmount = viewModel::onChangeAmount,
                onChangeSelectedCoin = viewModel::onChangeSelectedCoin,
            )
        }
        composable(
            route = Route.ALL_VALUES_SCREEN.route
        ) {
            AllValuesScreen(
                modifier = padding,
                conversionValueTexts = viewModel.getConversionValueTexts(shouldGetAllValues = true),
                lastUpdatedDate = lastUpdatedDate
            )
        }
    }
}