package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import com.dgomesdev.exchangeapp.presentation.ui.composables.AmountToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.CoinToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.ConversionValue
import com.dgomesdev.exchangeapp.presentation.ui.composables.OnChangeAmount
import com.dgomesdev.exchangeapp.presentation.ui.composables.OnChangeSelectedCoin

@Composable
fun ConversionScreen(
    modifier: Modifier,
    allValues: List<ExchangeModel>,
    onChangeAmount: OnChangeAmount,
    selectedCoin: Coin,
    onChangeSelectedCoin: OnChangeSelectedCoin,
    amountToBeConverted: Double
) {
    val conversionValues = allValues.filter { exchangeModel ->
        exchangeModel.code == selectedCoin.name
    }

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AmountToBeConverted(
            modifier = modifier,
            onChangeAmount = onChangeAmount,
        )
        CoinToBeConverted(
            modifier = modifier,
            selectedCoin = selectedCoin,
            onChangeSelectedCoin = onChangeSelectedCoin
        )
        Text(stringResource(R.string.exchange_values), modifier, fontSize = 24.sp)
        for (value in conversionValues) {
            ConversionValue(
                modifier = modifier,
                value = value,
                amountToBeConverted = amountToBeConverted
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionScreenPreview() {
    ConversionScreen(
        modifier = Modifier.padding(8.dp),
        allValues = listOf(),
        onChangeAmount = {},
        selectedCoin = Coin.BRL,
        onChangeSelectedCoin = {},
        amountToBeConverted = 5.0
    )
}