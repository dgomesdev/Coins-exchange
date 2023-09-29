package com.dgomesdev.exchangeapp.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dgomesdev.exchangeapp.domain.useCases.CoinConversionUseCase
import com.dgomesdev.exchangeapp.domain.useCases.GetLastUpdatedValuesIfNoNetworkUseCase
import com.dgomesdev.exchangeapp.domain.useCases.GetTodayValuesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExchangeViewModel @Inject constructor(
    private val coinConversionUseCase: CoinConversionUseCase,
    private val getTodayValuesUseCase: GetTodayValuesUseCase,
    private val getLastUpdatedValuesIfNoNetworkUseCase: GetLastUpdatedValuesIfNoNetworkUseCase,
) : ViewModel() {

    private val _conversionValuesList = MutableStateFlow<State?>(null)
    val conversionValuesList: StateFlow<State?> = _conversionValuesList

    private val _todayValuesList = MutableStateFlow(listOf(0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    val todayValuesList: StateFlow<List<Double>> = _todayValuesList

    private val _lastUpdateDate = MutableStateFlow("")
    val lastUpdateDate: StateFlow<String> = _lastUpdateDate

    init {
        getExchangeValues("USD")
        getTodayValues()
    }

    fun getExchangeValues(coin: String) {
        viewModelScope.launch {
            coinConversionUseCase(coin)
                .onStart {
                    _conversionValuesList.value = State.Loading
                }.catch {
                    _conversionValuesList.value = State.Failure(it)
                }.collect {
                    _conversionValuesList.value = State.Success(it)
                }
        }
    }

    private fun getTodayValues() {
        viewModelScope.launch {
            getTodayValuesUseCase()
                .catch {firstError ->
                    Log.e("FIRST ERROR", firstError.message ?: "NO FIRST ERROR")
                    getLastUpdatedValuesIfNoNetworkUseCase()
                        .catch {secondError ->
                            Log.e("SECOND ERROR", secondError.message ?: "NO SECOND ERROR")
                            _todayValuesList.value = listOf(1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
                        }.collect {
                            _todayValuesList.value = it.first
                            _lastUpdateDate.value = it.second
                        }
                }.collect {
                    _todayValuesList.value = it.first
                    _lastUpdateDate.value = it.second
                }
        }
    }
}

sealed class State {
    object Loading : State()
    data class Success(
        val valuesList: List<Double>,
    ) : State()

    data class Failure(
        val error: Throwable,
    ) : State()
}