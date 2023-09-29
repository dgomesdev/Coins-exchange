package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.sp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.presentation.screens.OnCoinConversion
import com.dgomesdev.exchangeapp.presentation.screens.OnConversionButtonClick
import com.dgomesdev.exchangeapp.presentation.ui.CoinList

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun AmountToBeConverted(
    modifier: Modifier,
    amountToBeConverted: (Double) -> Unit,
    onCoinConversion: OnCoinConversion,
    onConversionButtonClick: OnConversionButtonClick,
    coinToBeConverted: String
) {

    var amount by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue(""))
    }

    val keyBoard = LocalSoftwareKeyboardController.current

    TextField(
        value = amount,
        onValueChange = { amount = it ; amountToBeConverted(it.text.toDoubleOrNull() ?: 1.0) },
        modifier = modifier.fillMaxWidth(),
        label = { Text(stringResource(R.string.amount_to_be_converted)) },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
            imeAction = ImeAction.Go
        ),
        keyboardActions = KeyboardActions(
            onGo = { keyBoard?.hide() ; onCoinConversion(coinToBeConverted) ; onConversionButtonClick()}
        )
    )
}

@Composable
fun CoinToBeConverted(
    modifier: Modifier,
    convertedCoin: String,
    coinToBeConverted: (String) -> Unit
) {

    var selectedCoin by rememberSaveable {
        mutableStateOf(convertedCoin)
    }

    var expandedMenu by remember {
        mutableStateOf(false)
    }

    Column(modifier) {
        OutlinedButton(
            onClick = { expandedMenu = true }
        ) {
            Text(selectedCoin, fontSize = 20.sp)
            Icon(
                imageVector = if (!expandedMenu)
                    Icons.Default.ArrowDropDown else Icons.Default.KeyboardArrowUp,
                contentDescription = "Coin selection button"
            )
        }
        DropdownMenu(
            expanded = expandedMenu,
            onDismissRequest = { expandedMenu = false }
        ) {
            for (coin in CoinList.coinList) {
                if (coin != selectedCoin) {
                    DropdownMenuItem(
                        text = { Text(coin) },
                        onClick = {
                            coinToBeConverted(coin); selectedCoin = coin; expandedMenu = false
                        }
                    )
                }
            }
        }
    }
}