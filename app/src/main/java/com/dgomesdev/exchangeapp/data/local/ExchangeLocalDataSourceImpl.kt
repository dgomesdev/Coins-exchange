package com.dgomesdev.exchangeapp.data.local

import android.util.Log
import javax.inject.Inject

class ExchangeLocalDataSourceImpl @Inject constructor(
    val exchangeDao: ExchangeDao
) : ExchangeLocalDataSource {
    override suspend fun saveExchangeData(exchangeValues: List<ExchangeLocalEntity>) {
        Log.i("ExchangeLocalDataSource", "saveExchangeData: $exchangeValues")
        exchangeValues.forEach { exchangeDao.saveExchange(it) }
        exchangeDao.saveTimestamp(LastUpdatedTimestamp(timestamp = System.currentTimeMillis()))
    }

    override suspend fun getLastUpdatedTimestamp(): LastUpdatedTimestamp? {
        val timestamp = exchangeDao.getLastUpdatedTimestamp()
        Log.i("ExchangeLocalDataSource", "getLastUpdatedTimestamp: $timestamp")
        return timestamp
    }

    override suspend fun getAllExchangeValues(): List<ExchangeLocalEntity> {
        val entities =exchangeDao.getAllExchangeValues()
        Log.i("ExchangeLocalDataSource", "getAllExchangeValues: $entities")
        return entities
    }
}