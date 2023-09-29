package com.dgomesdev.exchangeapp.domain.useCases

import android.util.Log
import com.dgomesdev.exchangeapp.data.data.ExchangeRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetTodayValuesUseCase @Inject constructor(
    private val repository: ExchangeRepositoryImpl,
) {

    suspend operator fun invoke() = flow {

        val valuesList = mutableListOf<Double>()

        val exchangeValuesList = listOf(
        repository.getExchangeValues("USD-BRL").first(),
        repository.getExchangeValues("EUR-BRL").first(),
        repository.getExchangeValues("BRL-USD").first(),
        repository.getExchangeValues("EUR-USD").first(),
        repository.getExchangeValues("BRL-EUR").first(),
        repository.getExchangeValues("USD-EUR").first()
        )

        for (exchangeValue in exchangeValuesList) {
            try{ repository.save(exchangeValue) }
            catch (error: Throwable) { Log.e("ROOM ERROR", error.message ?: "No error") }
            finally { valuesList.add(exchangeValue.bid) }
        }

        emit(Pair(valuesList, exchangeValuesList.first().createDate))
    }
}