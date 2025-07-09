package com.dgomesdev.exchangeapp.data.repository

import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {

    fun getValues(conversionPairs: List<ConversionPair>): Flow<List<ExchangeModel>>
}