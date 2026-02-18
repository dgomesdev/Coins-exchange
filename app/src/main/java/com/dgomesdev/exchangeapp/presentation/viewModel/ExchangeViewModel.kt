package com.dgomesdev.exchangeapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.exchangeapp.data.repository.ExchangeRepository
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ConversionPair
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val repository: ExchangeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExchangeState())
    val uiState: StateFlow<ExchangeState> = _uiState

    init {
        getValues()
    }

    fun getValues() {
        viewModelScope.launch {
            repository.getValues(ConversionPair.entries).fold(
                onSuccess = { values ->
                    val lastUpdatedDate = repository.getFormattedLastUpdatedDate()
                    _uiState.update {
                        it.copy(
                            status = ExchangeStateStatus.SUCCESS,
                            values = values,
                            lastUpdatedDate = lastUpdatedDate
                        )
                    }
                },
                onFailure = {error ->
                    _uiState.update {
                        it.copy(
                            status = ExchangeStateStatus.ERROR,
                            errorMessage = error.message ?: "Erro ao caregar os valores de conversão"
                        )
                    }
                }
            )
        }
    }
    fun onChangeAmount(amount: String) {
        _uiState.update {
            it.copy(amountToBeConverted = amount)
        }
    }

    fun onChangeSelectedCoin(coin: Coin) {
        _uiState.update {
            it.copy(selectedCoin = coin)
        }
    }

    fun getConversionValueTexts(shouldGetAllValues: Boolean): List<String> {
        val state = _uiState.value
        val conversionValues = if (!shouldGetAllValues) {
            state.values.filter { exchangeModel ->
                exchangeModel.code == state.selectedCoin.name
            }
        } else {
                state.values
            }
        return conversionValues.map { exchangeModel ->
            setConversionText(
                conversionPair = exchangeModel.conversionPair,
                amountToBeConverted = if (shouldGetAllValues) 1.0 else state.amountToBeConverted.toDoubleOrNull() ?: 0.0,
                bid = exchangeModel.bid
            )
        }
    }

    private fun setConversionText(
        conversionPair: ConversionPair,
        amountToBeConverted: Double,
        bid: Double
    ): String = when (conversionPair) {
        ConversionPair.USDBRL -> "${amountToBeConverted.formatValue()} USD = ${(bid * amountToBeConverted).formatValue()} BRL"
        ConversionPair.EURBRL -> "${amountToBeConverted.formatValue()} EUR = ${(bid * amountToBeConverted).formatValue()} BRL"
        ConversionPair.BRLUSD -> "${amountToBeConverted.formatValue()} BRL = ${(bid * amountToBeConverted).formatValue()} USD"
        ConversionPair.EURUSD -> "${amountToBeConverted.formatValue()} EUR = ${(bid * amountToBeConverted).formatValue()} USD"
        ConversionPair.BRLEUR -> "${amountToBeConverted.formatValue()} BRL = ${(bid * amountToBeConverted).formatValue()} EUR"
        ConversionPair.USDEUR -> "${amountToBeConverted.formatValue()} USD = ${(bid * amountToBeConverted).formatValue()} EUR"
    }

    private fun Double.formatValue(): String = DecimalFormat("#,##0.00").format(this)
}