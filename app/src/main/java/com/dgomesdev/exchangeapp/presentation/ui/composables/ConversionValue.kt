package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ConversionValue(
    modifier: Modifier = Modifier,
    conversionText: String,
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(text = conversionText, modifier, style = MaterialTheme.typography.titleLarge)
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionValuePreview() {

}