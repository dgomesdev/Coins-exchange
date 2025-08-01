package com.dgomesdev.exchangeapp.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dgomesdev.exchangeapp.R
import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import com.dgomesdev.exchangeapp.presentation.ui.composables.ConversionValue
import com.dgomesdev.exchangeapp.presentation.ui.composables.ErrorButton
import com.dgomesdev.exchangeapp.presentation.ui.composables.LoadingIndicator
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeState
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeStateStatus
import com.dgomesdev.exchangeapp.presentation.viewModel.ExchangeViewModel
import kotlinx.coroutines.launch
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

@Composable
fun AllValuesScreen(
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

    AllValuesScreenContent(modifier, state, viewModel::getValues)
}

@Composable
fun AllValuesScreenContent(
    modifier: Modifier = Modifier,
    state: ExchangeState,
    retryAction: () -> Unit = {}
) {

    when (state.status) {
        ExchangeStateStatus.LOADING -> LoadingIndicator()
        ExchangeStateStatus.ERROR -> ErrorButton { retryAction }
        ExchangeStateStatus.SUCCESS -> {
            val now = ZonedDateTime.now(ZoneId.systemDefault())
            val formattedDate = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                .withLocale(Locale.getDefault())
            val lastUpdateDate = now.format(formattedDate)
            Column(
                modifier.fillMaxWidth(),
                Arrangement.Center,
                Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.last_update_values), modifier, fontSize = 20.sp)
                for (conversionValue in state.values) {
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
    }
}

@Preview(showBackground = true)
@Composable
fun AllValuesScreenPreview() {
    val fakeState = ExchangeState(
        status = ExchangeStateStatus.SUCCESS,
        values = listOf(
            ExchangeModel(ConversionPair.USDBRL, bid = 5.12, code = "USD"),
            ExchangeModel(ConversionPair.BRLUSD, bid = 0.19, code = "BRL"),
        ),
    )

    AllValuesScreenContent(
        modifier = Modifier.padding(8.dp),
        state = fakeState,
    )
}