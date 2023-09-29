package com.dgomesdev.exchangeapp.domain.useCases

import com.dgomesdev.exchangeapp.data.data.ExchangeRepositoryImpl
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetLastUpdatedValuesIfNoNetworkUseCase @Inject constructor(
    private val repository: ExchangeRepositoryImpl
) {

    suspend operator fun invoke() = flow {

        val valuesList: List<ExchangeValues> = repository.getLastUpdatedValues().first()

        val bidValuesList = mutableListOf<Double>()

        for (value in valuesList) {
            bidValuesList.add(value.bid)
        }

        emit (Pair(bidValuesList, valuesList.first().createDate))
    }
}