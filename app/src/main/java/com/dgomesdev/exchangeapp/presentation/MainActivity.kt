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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ExchangeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainState = viewModel.mainState.collectAsState().value
            val historyState = viewModel.historyState.collectAsState().value
            ExchangeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExchangeApp(
                        convertCoinsAction = viewModel::getExchangeValues,
                        saveExchangeValues = viewModel::saveExchangeValues,
                        exchangeValues = mainState,
                        exchangeValue = mainState?.bid ?: 1.0,
                        savedValues = historyState
                    )
                }
            }
        }
    }
}