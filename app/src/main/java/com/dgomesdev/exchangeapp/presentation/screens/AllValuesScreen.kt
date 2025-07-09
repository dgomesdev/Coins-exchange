package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import com.dgomesdev.exchangeapp.presentation.ui.composables.ConversionValue

@Composable
fun TodayConversionScreen(
    modifier: Modifier,
    valuesList: List<ExchangeModel>,
    lastUpdateDate: String
) {

    Column(
        modifier.fillMaxWidth(),
        Arrangement.Center,
        Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.last_update_values), modifier, fontSize = 20.sp)
        for (conversionValue in valuesList) {
            Row(
                modifier,
                Arrangement.Center,
                Alignment.CenterVertically
            ) {
                ConversionValue(value = conversionValue)
            }
        }
        Text(
            "${stringResource(R.string.last_update_at)} $lastUpdateDate",
            modifier,
            fontSize = 20.sp,
            textAlign = TextAlign.Center
        )
    }
}