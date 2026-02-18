package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.dgomesdev.exchangeapp.presentation.ui.composables.AmountToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.CoinToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.ConversionValue
import com.dgomesdev.exchangeapp.presentation.ui.theme.ExchangeAppTheme

@Composable
fun ConversionScreen(
    modifier: Modifier = Modifier,
    amountToBeConverted: String = "10.0",
    selectedCoin: Coin = Coin.BRL,
    conversionValueTexts: List<String> = emptyList(),
    onChangeAmount: (String) -> Unit = {},
    onChangeSelectedCoin: (Coin) -> Unit = {}
) {
    ConversionScreenPortraitLayout(
        modifier,
        amountToBeConverted,
        selectedCoin,
        conversionValueTexts,
        onChangeAmount,
        onChangeSelectedCoin
    )
}

@Composable
fun ConversionScreenPortraitLayout(
    modifier: Modifier = Modifier,
    amountToBeConverted: String = "10.0",
    selectedCoin: Coin = Coin.BRL,
    conversionValueTexts: List<String> = emptyList(),
    onChangeAmount: (String) -> Unit = {},
    onChangeSelectedCoin: (Coin) -> Unit = {}
) {
    Column(
        modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AmountToBeConverted(
            modifier = modifier,
            amountToBeConverted = amountToBeConverted,
            onChangeAmount = onChangeAmount,
        )
        CoinToBeConverted(
            modifier = modifier,
            selectedCoin = selectedCoin,
            onChangeSelectedCoin = onChangeSelectedCoin
        )
        Text(stringResource(R.string.exchange_values), modifier, fontSize = 24.sp)
        conversionValueTexts.forEach { conversionText ->
            ConversionValue(
                modifier = modifier,
                conversionText = conversionText
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionScreenPreview() {
    val conversionValueTexts = listOf(
        "100.00 BRL = 100.00 USD",
        "100.00 BRL = 100.00 EUR",
    )
    ExchangeAppTheme {
        ConversionScreen(
            modifier = Modifier.padding(8.dp),
            conversionValueTexts = conversionValueTexts
        )
    }
}

@Preview(showBackground = true, heightDp = 400, widthDp = 800)
@Composable
fun ConversionScreenPreviewLandscape() {
    val conversionValueTexts = listOf(
        "100.00 BRL = 100.00 USD",
        "100.00 BRL = 100.00 EUR",
    )
    ExchangeAppTheme {
        ConversionScreen(
            modifier = Modifier.padding(8.dp),
            conversionValueTexts = conversionValueTexts
        )
    }
}