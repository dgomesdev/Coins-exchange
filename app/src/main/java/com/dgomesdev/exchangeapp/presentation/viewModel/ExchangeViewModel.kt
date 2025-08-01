package com.dgomesdev.exchangeapp.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.exchangeapp.data.repository.ExchangeRepository
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ConversionPair
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
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
        val conversionPairs = ConversionPair.entries
        viewModelScope.launch {
            repository.getValues(conversionPairs)
                .onStart { _uiState.value = _uiState.value.copy(status = ExchangeStateStatus.LOADING) }
                .catch { error ->
                    _uiState.value = _uiState.value.copy(
                        status = ExchangeStateStatus.ERROR,
                        errorMessage = error.message ?: "Erro ao caregar os valores de conversão"
                    )
                }
                .collect { values ->
                    _uiState.value = _uiState.value.copy(
                        status = ExchangeStateStatus.SUCCESS,
                        values = values
                    )
                }
        }
    }

    fun onChangeAmount(amount: String) {
        _uiState.value = (_uiState.value.copy(amountToBeConverted = amount.toDoubleOrNull() ?: 0.0))
    }

    fun onChangeSelectedCoin(coin: Coin) {
        _uiState.value = _uiState.value.copy(selectedCoin = coin)
    }
}