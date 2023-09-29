package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.presentation.ui.composables.AmountToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.CoinToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.ConversionValue

typealias OnCoinConversion = (String) -> Unit
typealias OnConversionButtonClick = () -> Unit

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConversionScreen(
    modifier: Modifier,
    convertedValues: List<Double>,
    onCoinConversion: OnCoinConversion,
    onConversionButtonClick: OnConversionButtonClick,
) {

    var coinToBeConverted by rememberSaveable {
        mutableStateOf("USD")
    }
    var amountToBeConverted by rememberSaveable {
        mutableStateOf(1.0)
    }
    val keyBoard = LocalSoftwareKeyboardController.current

    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AmountToBeConverted(
            modifier = modifier,
            amountToBeConverted = { amountToBeConverted = it },
            onCoinConversion = onCoinConversion,
            onConversionButtonClick = onConversionButtonClick,
            coinToBeConverted = coinToBeConverted
        )
        CoinToBeConverted(
            modifier = modifier,
            convertedCoin = coinToBeConverted,
            coinToBeConverted = { coinToBeConverted = it }
        )
        Button(
            onClick = {
                onCoinConversion(coinToBeConverted);
                onConversionButtonClick();
                keyBoard?.hide()
            },
            modifier
        ) {
            Text(stringResource(R.string.convert), fontSize = 18.sp)
        }
        Text(stringResource(R.string.exchange_values), modifier, fontSize = 24.sp)
        ConversionValue(
            modifier = modifier,
            value = convertedValues[0] * amountToBeConverted,
            coin = "R\$"
        )
        ConversionValue(
            modifier = modifier,
            value = convertedValues[1] * amountToBeConverted,
            coin = "\$"
        )
        ConversionValue(
            modifier = modifier,
            value = convertedValues[2] * amountToBeConverted,
            coin = "€"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionScreenPreview() {
    ConversionScreen(
        modifier = Modifier.padding(8.dp),
        convertedValues = listOf(1.0, 1.0, 1.0),
        onCoinConversion = {},
        {})
}