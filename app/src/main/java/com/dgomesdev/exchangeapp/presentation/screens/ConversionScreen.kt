package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.presentation.ui.composables.AmountToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.CoinToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.ConversionValue
import com.dgomesdev.exchangeapp.presentation.ui.theme.ExchangeAppTheme

@Composable
fun ConversionScreen(
    modifier: Modifier = Modifier,
    isLandscape: Boolean,
    amountToBeConverted: String = "10.0",
    selectedCoin: Coin = Coin.BRL,
    conversionValueTexts: List<String> = emptyList(),
    onChangeAmount: (String) -> Unit = {},
    onChangeSelectedCoin: (Coin) -> Unit = {}
) {
    if (isLandscape) {
        ConversionScreenLandscapeLayout(
            modifier,
            amountToBeConverted,
            selectedCoin,
            conversionValueTexts,
            onChangeAmount,
            onChangeSelectedCoin
        )
    } else {
        ConversionScreenPortraitLayout(
            modifier,
            amountToBeConverted,
            selectedCoin,
            conversionValueTexts,
            onChangeAmount,
            onChangeSelectedCoin
        )
    }
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
    val scrollState = rememberScrollState()
    Column(
        modifier.fillMaxWidth().verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AmountToBeConverted(
            modifier = Modifier.padding(8.dp),
            amountToBeConverted = amountToBeConverted,
            onChangeAmount = onChangeAmount,
        )
        CoinToBeConverted(
            modifier = Modifier.padding(8.dp),
            selectedCoin = selectedCoin,
            onChangeSelectedCoin = onChangeSelectedCoin
        )
        Text(
            stringResource(R.string.exchange_values),
            Modifier.padding(8.dp),
            style = MaterialTheme.typography.titleLarge
        )
        conversionValueTexts.forEach { conversionText ->
            ConversionValue(
                modifier = Modifier.padding(8.dp),
                conversionText = conversionText
            )
        }
    }
}
@Composable
fun ConversionScreenLandscapeLayout(
    modifier: Modifier = Modifier,
    amountToBeConverted: String = "10.0",
    selectedCoin: Coin = Coin.BRL,
    conversionValueTexts: List<String> = emptyList(),
    onChangeAmount: (String) -> Unit = {},
    onChangeSelectedCoin: (Coin) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    Row(
        modifier.fillMaxWidth().verticalScroll(scrollState),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            Modifier.padding(8.dp).weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AmountToBeConverted(
                modifier = Modifier.padding(8.dp),
                amountToBeConverted = amountToBeConverted,
                onChangeAmount = onChangeAmount,
            )
            CoinToBeConverted(
                modifier = Modifier.padding(8.dp),
                selectedCoin = selectedCoin,
                onChangeSelectedCoin = onChangeSelectedCoin
            )
        }
        Column(
            Modifier.padding(8.dp).weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(stringResource(
                R.string.exchange_values),
                Modifier.padding(8.dp),
                style = MaterialTheme.typography.titleLarge
            )
            conversionValueTexts.forEach { conversionText ->
                ConversionValue(
                    modifier = Modifier.padding(8.dp),
                    conversionText = conversionText
                )
            }
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
            conversionValueTexts = conversionValueTexts,
            isLandscape = false
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
            isLandscape = true,
            conversionValueTexts = conversionValueTexts
        )
    }
}