package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import java.text.DecimalFormat

@Composable
fun HistoryScreen(
    modifier: Modifier,
    savedValues: List<ExchangeValues>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(savedValues) {
            Text("${DecimalFormat("#,#00.00").format(it.convertedAmount)} ${it.code} = ${DecimalFormat("#,#00.00").format(it.bid * it.convertedAmount)} ${it.codein}" , modifier.padding(top = 8.dp))
        }
    }
}