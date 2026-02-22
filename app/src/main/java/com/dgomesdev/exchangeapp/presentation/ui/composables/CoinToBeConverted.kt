package com.dgomesdev.exchangeapp.presentation.ui.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.dgomesdev.exchangeapp.domain.Coin

@Composable
fun CoinToBeConverted(
    modifier: Modifier,
    selectedCoin: Coin,
    onChangeSelectedCoin: (Coin) -> Unit
) {
    var expandedMenu by remember { mutableStateOf(false) }

    Column(modifier) {
        OutlinedButton(
            onClick = { expandedMenu = true }
        ) {
            Text(
                selectedCoin.name,
                style = MaterialTheme.typography.titleLarge
                )
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
            for (coin in Coin.entries) {
                if (coin != selectedCoin) {
                    DropdownMenuItem(
                        text = { Text(coin.name) },
                        onClick = {
                            onChangeSelectedCoin(coin)
                            expandedMenu = false
                        }
                    )
                }
            }
        }
    }
}
