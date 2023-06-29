package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import com.dgomesdev.exchangeapp.presentation.screens.ConvertCoinsAction
import com.dgomesdev.exchangeapp.presentation.screens.HistoryScreen
import com.dgomesdev.exchangeapp.presentation.screens.MainScreen
import com.dgomesdev.exchangeapp.presentation.screens.SaveExchangeValues

@Composable
fun ExchangeNavHost(
    navController: NavHostController,
    modifier: Modifier,
    convertCoinsAction: ConvertCoinsAction,
    saveExchangeValues: SaveExchangeValues,
    exchangeValues: ExchangeValues?,
    bidValue: Double,
    savedValues: List<ExchangeValues>
) {
    NavHost(
        navController = navController,
        startDestination = "Main screen"
    ) {
        composable(
            route = "Main screen"
        ) {
            MainScreen(
                modifier = modifier.padding(16.dp),
                convertCoinsAction = convertCoinsAction,
                saveExchangeValues = saveExchangeValues,
                exchangeValues = exchangeValues,
                bidValue = bidValue
            )
        }
        composable(
            route = "History screen"
        ) {
            HistoryScreen(
                modifier = modifier.padding(16.dp),
                savedValues = savedValues
            )
        }
    }
}