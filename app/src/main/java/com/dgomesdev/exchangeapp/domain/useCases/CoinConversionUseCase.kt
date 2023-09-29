package com.dgomesdev.exchangeapp.domain.useCases

import com.dgomesdev.exchangeapp.data.data.ExchangeRepositoryImpl
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinConversionUseCase @Inject constructor(
    private val repository: ExchangeRepositoryImpl,
) {

    suspend operator fun invoke(coin: String) = execute(coin)

    private suspend fun execute(coin: String): Flow<List<Double>> = flow {

        val valuesList: MutableList<Double> = mutableListOf()
        when (coin) {
            "BRL" -> {
                valuesList.add(1.0)
                valuesList.add(repository.getExchangeValues("BRL-USD").first().bid)
                valuesList.add(repository.getExchangeValues("BRL-EUR").first().bid)
            }

            "USD" -> {
                valuesList.add(repository.getExchangeValues("USD-BRL").first().bid)
                valuesList.add(1.0)
                valuesList.add(repository.getExchangeValues("USD-EUR").first().bid)
            }

            "EUR" -> {
                valuesList.add(repository.getExchangeValues("EUR-BRL").first().bid)
                valuesList.add(repository.getExchangeValues("EUR-USD").first().bid)
                valuesList.add(1.0)
            }

            else -> {
                valuesList.add(1.0)
                valuesList.add(1.0)
                valuesList.add(1.0)
            }
        }
        emit(valuesList)
    }
}