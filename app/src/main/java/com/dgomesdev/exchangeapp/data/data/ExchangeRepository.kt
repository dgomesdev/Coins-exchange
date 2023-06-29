package com.dgomesdev.exchangeapp.data.data

import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import kotlinx.coroutines.flow.Flow

interface ExchangeRepository {

    suspend fun getExchangeValues(coins: String): Flow<ExchangeValues>

    suspend fun save(exchangeValues: ExchangeValues)

    fun list(): Flow<List<ExchangeValues>>
}