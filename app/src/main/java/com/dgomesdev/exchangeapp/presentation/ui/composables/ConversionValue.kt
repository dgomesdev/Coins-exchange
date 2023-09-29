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
import java.text.DecimalFormat

@Composable
fun ConversionValue(
    modifier: Modifier = Modifier,
    value: Double,
    coin: String
) {

    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = "${value.formatNumber()} $coin", modifier, fontSize = 20.sp)
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionValuePreview() {

    ConversionValue(
        Modifier.padding(8.dp),
        10.00000,
        "USD"
    )
}

fun Double.formatNumber(): String = DecimalFormat("#,##0.00").format(this)