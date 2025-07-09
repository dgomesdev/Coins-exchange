package com.dgomesdev.exchangeapp.presentation.viewModel

import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ExchangeModel

data class ExchangeState(
    val values: List<ExchangeModel> = listOf(),
    val status: ExchangeStateStatus = ExchangeStateStatus.SUCCESS,
    val errorMessage: String = "",
    val selectedCoin: Coin = Coin.BRL,
    val amountToBeConverted: Double = 0.0
)

enum class ExchangeStateStatus {
    LOADING,
    SUCCESS,
    ERROR
}