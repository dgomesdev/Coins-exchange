package com.dgomesdev.exchangeapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import com.dgomesdev.exchangeapp.presentation.ui.composables.ExchangeApp
import com.dgomesdev.exchangeapp.presentation.ui.theme.ExchangeAppTheme
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ExchangeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val now = ZonedDateTime.now(ZoneId.systemDefault())
        val formattedDate = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
        val lastUpdateDate = now.format(formattedDate)
        setContent {
            val uiState = viewModel.uiState.collectAsState().value
            ExchangeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExchangeApp(
                        allValues = uiState.values,
                        lastUpdateDate = lastUpdateDate,
                        onChangeAmount = viewModel::onChangeAmount,
                        selectedCoin = uiState.selectedCoin,
                        onChangeSelectedCoin = viewModel::onChangeSelectedCoin,
                        amountToBeConverted = uiState.amountToBeConverted
                    )
                }
            }
        }
    }
}