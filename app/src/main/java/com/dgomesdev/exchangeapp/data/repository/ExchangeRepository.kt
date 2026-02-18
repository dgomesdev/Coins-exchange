package com.dgomesdev.exchangeapp.data.repository

import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel

interface ExchangeRepository {

    suspend fun getValues(conversionPairs: List<ConversionPair>): Result<List<ExchangeModel>>
    suspend fun getFormattedLastUpdatedDate(): String
}