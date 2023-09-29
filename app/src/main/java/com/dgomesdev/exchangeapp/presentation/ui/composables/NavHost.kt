package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.dgomesdev.exchangeapp.presentation.screens.ConversionScreen
import com.dgomesdev.exchangeapp.presentation.screens.OnCoinConversion
import com.dgomesdev.exchangeapp.presentation.screens.OnConversionButtonClick
import com.dgomesdev.exchangeapp.presentation.screens.TodayConversionScreen

@Composable
fun ExchangeNavHost(
    navController: NavHostController,
    modifier: Modifier,
    convertedValues: List<Double>,
    todayValues: List<Double>,
    lastUpdateDate: String,
    onCoinConversion: OnCoinConversion,
    onConversionButtonClick: OnConversionButtonClick
) {
    val padding = Modifier.padding(8.dp)
    NavHost(
        navController = navController,
        startDestination = "Conversion screen",
        modifier = modifier
    ) {
        composable(
            route = "Conversion screen"
        ) {
            ConversionScreen(
                modifier = padding,
                convertedValues = convertedValues,
                onCoinConversion = onCoinConversion,
                onConversionButtonClick = onConversionButtonClick
            )
        }
        composable(
            route = "Today's values screen"
        ) {
            TodayConversionScreen(
                modifier = padding,
                valuesList = todayValues,
                lastUpdateDate = lastUpdateDate
            )
        }
    }
}