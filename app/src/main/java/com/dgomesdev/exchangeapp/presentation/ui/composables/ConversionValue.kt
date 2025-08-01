package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import java.text.DecimalFormat

@Composable
fun ConversionValue(
    modifier: Modifier = Modifier,
    value: ExchangeModel,
    amountToBeConverted: Double = 1.0
) {

    val conversionText = when (value.conversionPair) {
        ConversionPair.USDBRL -> "${amountToBeConverted.formatValue()} USD = ${(value.bid * amountToBeConverted).formatValue()} BRL"
        ConversionPair.EURBRL -> "${amountToBeConverted.formatValue()} EUR = ${(value.bid * amountToBeConverted).formatValue()} BRL"
        ConversionPair.BRLUSD -> "${amountToBeConverted.formatValue()} BRL = ${(value.bid * amountToBeConverted).formatValue()} USD"
        ConversionPair.EURUSD -> "${amountToBeConverted.formatValue()} EUR = ${(value.bid * amountToBeConverted).formatValue()} USD"
        ConversionPair.BRLEUR -> "${amountToBeConverted.formatValue()} BRL = ${(value.bid * amountToBeConverted).formatValue()} EUR"
        ConversionPair.USDEUR -> "${amountToBeConverted.formatValue()} USD = ${(value.bid * amountToBeConverted).formatValue()} EUR"
    }

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = conversionText, modifier, fontSize = 20.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionValuePreview() {

    ConversionValue(
        Modifier.padding(8.dp),
        ExchangeModel(ConversionPair.BRLEUR, 2.0, code = "BRL"),
        5.0
    )
}

fun Double.formatValue(): String = DecimalFormat("#,##0.00").format(this)