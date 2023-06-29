package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import com.dgomesdev.exchangeapp.presentation.ui.CoinList
import java.text.NumberFormat
import java.util.Locale

typealias AmountToBeConverted = (Double) -> Unit
typealias ConvertCoinsAction = (String) -> Unit
typealias SaveExchangeValues = (ExchangeValues, Double) -> Unit

@Composable
fun MainScreen(
    modifier: Modifier,
    convertCoinsAction: ConvertCoinsAction,
    saveExchangeValues: SaveExchangeValues,
    exchangeValues: ExchangeValues?,
    bidValue: Double,
) {
    var coinFrom by rememberSaveable {
        mutableStateOf("EUR")
    }
    var coinTo by rememberSaveable {
        mutableStateOf("USD")
    }
    var amount by rememberSaveable {
        mutableStateOf(0.0)
    }
    var convertedAmount by rememberSaveable {
        mutableStateOf(0.0)
    }
    val isConversionValid = coinFrom != coinTo
    Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AmountTextField(modifier = modifier.fillMaxWidth(), amountToBeConverted = {amount = it})
        Row(modifier) {
            CoinSelectionButton(coinToBeConverted = {coinFrom = it}, defaultCoin = coinFrom, isCoinFrom = true)
            Spacer(modifier = modifier.padding(horizontal = 24.dp))
            CoinSelectionButton(coinToBeConverted = {coinTo = it}, defaultCoin = coinTo, isCoinFrom = false)
        }
        if (!isConversionValid) AlertText(modifier = modifier)
        ConvertButton(
            modifier = modifier,
            convertCoinsAction = convertCoinsAction,
            coinsToBeConverted = "$coinFrom-$coinTo",
            isEnabled = isConversionValid,
            convertedAmount = amount * bidValue,
            returnConvertedAmount = { convertedAmount = it }
        )
        SaveButton(
            modifier = modifier,
            saveExchangeValues = saveExchangeValues,
            exchangeValues = exchangeValues,
            isEnabled = isConversionValid,
            convertedAmount = convertedAmount
        )
        ExchangeText(
            modifier = modifier,
            exchangeValue = convertedAmount,
            coinFormat = coinTo
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AmountTextField(
    modifier: Modifier,
    amountToBeConverted: AmountToBeConverted
) {
    var amount by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }
    TextField(
        value = amount,
        onValueChange = { amount = it ; amountToBeConverted(it.text.toDoubleOrNull() ?: 0.0) },
        label = { Text(stringResource(R.string.amount_to_be_converted)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
        modifier = modifier
            .fillMaxWidth()
    )
}

@Composable
fun CoinSelectionButton(
    coinToBeConverted: (String) -> Unit,
    defaultCoin: String,
    isCoinFrom: Boolean
) {
    var selectedCoin by rememberSaveable {
        mutableStateOf(defaultCoin)
    }
    var expandedMenu by remember {
        mutableStateOf(false)
    }
    Column {
        Text(if (isCoinFrom) stringResource(R.string.from) else (stringResource(R.string.to)))
        OutlinedButton(
            onClick = { expandedMenu = true }
        ) {
            Text(selectedCoin)
            Icon(
                imageVector = if (!expandedMenu)
                    Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Coin selection"
            )
        }
        DropdownMenu(
            expanded = expandedMenu,
            onDismissRequest = { expandedMenu = false }
        ) {
            for (coin in CoinList.coinList) {
                DropdownMenuItem(
                    text = { Text(coin) },
                    onClick = { coinToBeConverted(coin); selectedCoin = coin }
                )
            }
        }
    }
}

@Composable
fun AlertText(modifier: Modifier) {
    Text(
        stringResource(R.string.a_coin_can_t_be_converted_to_itself),
        color = Color.Red,
        modifier = modifier
    )
}

@Composable
fun ConvertButton(
    modifier: Modifier,
    convertCoinsAction: ConvertCoinsAction,
    coinsToBeConverted: String,
    isEnabled: Boolean,
    convertedAmount: Double,
    returnConvertedAmount: (Double) -> Unit
) {
    ElevatedButton(
        onClick = { convertCoinsAction(coinsToBeConverted) ; returnConvertedAmount(convertedAmount) },
        enabled = isEnabled,
        modifier = modifier
    ) {
        Text(stringResource(R.string.convert))
    }
}

@Composable
fun SaveButton(
    modifier: Modifier,
    saveExchangeValues: SaveExchangeValues,
    exchangeValues: ExchangeValues?,
    isEnabled: Boolean,
    convertedAmount: Double
) {
    ElevatedButton(
        onClick = { saveExchangeValues(exchangeValues!!, convertedAmount) },
        enabled = if (exchangeValues == null) false else isEnabled,
        modifier = modifier
    ) {
        Text(stringResource(R.string.save))
    }
}

@Composable
fun ExchangeText(
    modifier: Modifier,
    exchangeValue: Double,
    coinFormat: String
) {
    val formattedAmount = when (coinFormat) {
        "EUR" -> NumberFormat.getCurrencyInstance(Locale.FRANCE).format(exchangeValue)
        "BRL" -> NumberFormat.getCurrencyInstance(Locale("pt", "BR")).format(exchangeValue)
        "GBP" -> NumberFormat.getCurrencyInstance(Locale.UK).format(exchangeValue)
        else -> NumberFormat.getCurrencyInstance(Locale.US).format(exchangeValue)
    }

    Text(formattedAmount, modifier = modifier)
}