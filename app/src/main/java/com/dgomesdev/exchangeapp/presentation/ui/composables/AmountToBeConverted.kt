package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.dgomesdev.exchangeapp.R

@Composable
fun AmountToBeConverted(
    modifier: Modifier,
    onChangeAmount: OnChangeAmount
) {
    var amountToBeConverted by rememberSaveable {
        mutableStateOf("")
    }
    TextField(
        value = amountToBeConverted,
        onValueChange = {
            amountToBeConverted = it
            onChangeAmount(it)
        },
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.amount_to_be_converted)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}