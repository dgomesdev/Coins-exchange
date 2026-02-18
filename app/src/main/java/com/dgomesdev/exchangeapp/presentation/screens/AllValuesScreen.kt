package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.presentation.ui.composables.ConversionValue
import com.dgomesdev.exchangeapp.presentation.ui.theme.ExchangeAppTheme

@Composable
fun AllValuesScreen(
    modifier: Modifier = Modifier,
    conversionValueTexts: List<String> = emptyList(),
    lastUpdatedDate: String = "Now"
) {
    Column(
        modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.last_update_values), modifier, fontSize = 20.sp)
        conversionValueTexts.forEach { conversionText ->
            Row(
                modifier,
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                ConversionValue(conversionText = conversionText)
            }
        }
        Text(
            "${stringResource(R.string.last_update_at)} $lastUpdatedDate",
            modifier,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AllValuesPreview() {
    val conversionValueTexts = listOf(
        "100.00 USD = 100.00 BRL",
        "100.00 EUR = 100.00 BRL",
        "100.00 BRL = 100.00 USD",
        "100.00 EUR = 100.00 USD",
        "100.00 BRL = 100.00 EUR",
        "100.00 USD = 100.00 EUR"
    )
    ExchangeAppTheme {
        AllValuesScreen(
            modifier = Modifier.padding(8.dp),
            conversionValueTexts = conversionValueTexts
        )
    }
}