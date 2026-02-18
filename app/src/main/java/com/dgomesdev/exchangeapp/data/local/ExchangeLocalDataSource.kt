package com.dgomesdev.exchangeapp.data.local

interface ExchangeLocalDataSource {
    suspend fun saveExchangeData(exchangeValues: List<ExchangeLocalEntity>)
    suspend fun getLastUpdatedTimestamp(): LastUpdatedTimestamp?

    suspend fun getAllExchangeValues(): List<ExchangeLocalEntity>
}