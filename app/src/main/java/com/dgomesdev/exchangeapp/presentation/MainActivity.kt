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
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.presentation.ui.composables.ExchangeApp
import com.dgomesdev.exchangeapp.presentation.ui.theme.ExchangeAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ExchangeViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            lateinit var valuesList: List<Double>
            lateinit var statusMessage: String
            val todayValues = viewModel.todayValuesList.collectAsState().value
            val lastUpdateDate = viewModel.lastUpdateDate.collectAsState().value
            when (val valuesListState = viewModel.conversionValuesList.collectAsState().value) {
                is State.Success -> {
                    valuesList = valuesListState.valuesList
                    statusMessage = getString(R.string.success)
                }
                is State.Failure -> {
                    valuesList = listOf(0.0, 0.0, 0.0)
                    statusMessage = getString(R.string.error)
                }
                State.Loading -> {
                    valuesList = listOf(0.0, 0.0, 0.0)
                    statusMessage = getString(R.string.loading)
                }
                null -> {}
            }
            ExchangeAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ExchangeApp(
                        convertedValues = valuesList,
                        todayValues = todayValues,
                        lastUpdateDate = lastUpdateDate,
                        onCoinConversion = viewModel::getExchangeValues,
                        statusMessage = statusMessage
                    )
                }
            }
        }
    }
}