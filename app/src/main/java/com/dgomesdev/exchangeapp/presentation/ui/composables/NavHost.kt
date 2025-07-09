package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import com.dgomesdev.exchangeapp.presentation.screens.ConversionScreen
import com.dgomesdev.exchangeapp.presentation.screens.TodayConversionScreen
import com.dgomesdev.exchangeapp.presentation.ui.Route

@Composable
fun ExchangeNavHost(
    navController: NavHostController,
    modifier: Modifier,
    allValues: List<ExchangeModel>,
    lastUpdateDate: String,
    onChangeAmount: OnChangeAmount,
    selectedCoin: Coin,
    onChangeSelectedCoin: OnChangeSelectedCoin,
    amountToBeConverted: Double
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
                allValues = allValues,
                onChangeAmount = onChangeAmount,
                selectedCoin = selectedCoin,
                onChangeSelectedCoin = onChangeSelectedCoin,
                amountToBeConverted = amountToBeConverted
            )
        }
        composable(
            route = Route.ALL_VALUES_SCREEN.route
        ) {
            TodayConversionScreen(
                modifier = padding,
                valuesList = allValues,
                lastUpdateDate = lastUpdateDate
            )
        }
    }
}