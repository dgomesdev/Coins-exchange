package com.dgomesdev.exchangeapp.data.local

import kotlinx.coroutines.flow.Flow

interface ExchangeLocalDataSource {
    fun getAll(): Flow<List<ExchangeLocalEntity>>

    suspend fun save(exchangeValues: ExchangeLocalEntity)
}