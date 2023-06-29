package com.dgomesdev.exchangeapp.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.exchangeapp.data.data.ExchangeRepositoryImpl
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val exchangeRepository: ExchangeRepositoryImpl
) : ViewModel() {

    private val _mainState = MutableStateFlow<ExchangeValues?>(null)
    val mainState: StateFlow<ExchangeValues?> = _mainState

    private val _historyState = MutableStateFlow<List<ExchangeValues>>(emptyList())
    val historyState: StateFlow<List<ExchangeValues>> = _historyState

    init {
        viewModelScope.launch {
            exchangeRepository.list()
                .collect{
                    _historyState.value = it
                }
        }
    }

    fun getExchangeValues(coins: String) = viewModelScope.launch {
        exchangeRepository.getExchangeValues(coins)
            .collect {
                _mainState.value = it
            }
    }

    fun saveExchangeValues(exchangeValues: ExchangeValues, convertedAmount: Double) {
        viewModelScope.launch {
            exchangeRepository.save(exchangeValues.copy(convertedAmount = convertedAmount))
            exchangeRepository.list()
                .collect {valuesList ->
                    _historyState.value = valuesList
                }
        }
    }
}