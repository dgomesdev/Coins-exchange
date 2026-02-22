package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeViewModel

@Composable
fun LargeScreenLayout(
    modifier: Modifier,
    viewModel: ExchangeViewModel,
    amountToBeConverted: String,
    selectedCoin: Coin,
    lastUpdatedDate: String
) {
    Row(
        modifier.padding(8.dp).fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConversionScreen(
            modifier = Modifier.weight(1f).padding(8.dp),
            isLandscape = false,
            amountToBeConverted = amountToBeConverted,
            selectedCoin = selectedCoin,
            conversionValueTexts = viewModel.getConversionValueTexts(shouldGetAllValues = false),
            onChangeAmount = viewModel::onChangeAmount,
            onChangeSelectedCoin = viewModel::onChangeSelectedCoin,
        )
        AllValuesScreen(
            modifier = Modifier.weight(1f).padding(8.dp),
            conversionValueTexts = viewModel.getConversionValueTexts(shouldGetAllValues = true),
            lastUpdatedDate = lastUpdatedDate
        )
    }
}