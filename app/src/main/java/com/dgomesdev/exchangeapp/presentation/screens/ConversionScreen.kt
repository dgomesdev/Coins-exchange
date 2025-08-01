package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import com.dgomesdev.exchangeapp.presentation.ui.composables.AmountToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.CoinToBeConverted
import com.dgomesdev.exchangeapp.presentation.ui.composables.ConversionValue
import com.dgomesdev.exchangeapp.presentation.ui.composables.ErrorButton
import com.dgomesdev.exchangeapp.presentation.ui.composables.LoadingIndicator
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeState
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeStateStatus
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeViewModel
import kotlinx.coroutines.launch

@Composable
fun ConversionScreen(
    modifier: Modifier,
    viewModel: ExchangeViewModel,
    snackbarHostState: SnackbarHostState
) {
    val state by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val retryLabel = stringResource(R.string.retry)

    LaunchedEffect(state.status) {
        if (state.status == ExchangeStateStatus.ERROR) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = state.errorMessage,
                    actionLabel = retryLabel
                ).also {
                    if (it == SnackbarResult.ActionPerformed) {
                        viewModel.getValues()
                    }
                }
            }
        }
    }

    ConversionScreenContent(
        modifier,
        state,
        viewModel::onChangeAmount,
        viewModel::onChangeSelectedCoin,
        viewModel::getValues
    )
}

@Composable
fun ConversionScreenContent(
    modifier: Modifier = Modifier,
    state: ExchangeState,
    onChangeAmount: (String) -> Unit = {},
    onChangeSelectedCoin: (Coin) -> Unit = {},
    retryAction: () -> Unit = {}
) {

    when (state.status) {
        ExchangeStateStatus.LOADING -> LoadingIndicator()
        ExchangeStateStatus.ERROR -> ErrorButton { retryAction }
        ExchangeStateStatus.SUCCESS -> {
            val conversionValues = state.values.filter { exchangeModel ->
                exchangeModel.code == state.selectedCoin.name
            }

            Column(modifier, horizontalAlignment = Alignment.CenterHorizontally) {
                AmountToBeConverted(
                    modifier = modifier,
                    onChangeAmount = onChangeAmount
                )
                CoinToBeConverted(
                    modifier = modifier,
                    selectedCoin = state.selectedCoin,
                    onChangeSelectedCoin = onChangeSelectedCoin
                )
                Text(stringResource(R.string.exchange_values), modifier, fontSize = 24.sp)
                for (value in conversionValues) {
                    ConversionValue(
                        modifier = modifier,
                        value = value,
                        amountToBeConverted = state.amountToBeConverted
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ConversionScreenPreview() {
    val fakeState = ExchangeState(
        status = ExchangeStateStatus.SUCCESS,
        values = listOf(
            ExchangeModel(ConversionPair.USDBRL, bid = 5.12, code = "USD"),
            ExchangeModel(ConversionPair.BRLUSD, bid = 0.19, code = "BRL")
        ),
        amountToBeConverted = 10.0
    )

    ConversionScreenContent(
        modifier = Modifier.padding(8.dp),
        state = fakeState,
    )
}