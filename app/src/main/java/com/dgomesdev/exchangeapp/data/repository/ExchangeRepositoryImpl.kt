package com.dgomesdev.exchangeapp.data.repository

import android.util.Log
import com.dgomesdev.exchangeapp.data.local.ExchangeLocalDataSource
import com.dgomesdev.exchangeapp.data.remote.ExchangeRemoteDataSource
import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import com.dgomesdev.exchangeapp.domain.InvalidCoinsException
import com.dgomesdev.exchangeapp.domain.RepositoryError
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExchangeRemoteDataSource,
    private val localDataSource: ExchangeLocalDataSource
) : ExchangeRepository {
    override suspend fun getValues(conversionPairs: List<ConversionPair>): Result<List<ExchangeModel>> {
        return runCatching {
            if (conversionPairs.isEmpty()) {
                throw InvalidCoinsException("Coins list can't be empty")
            }

            try {
                if (!shouldFetchFromApi()) {
                    Log.d("ExchangeRepositoryImpl", "Fetching from local database")
                    return@runCatching fetchFromLocalDatabase()
                }

                val coinsList: String = conversionPairs.joinToString(separator = ",") { it.coins }
                val remoteResponse = remoteDataSource.getExchangeValues(coinsList).getOrElse {
                    return@runCatching try {
                        fetchFromLocalDatabase()
                    } catch (e: Exception) {
                        throw RepositoryError("${e.message}", e)
                    }
                }
                Log.d("ExchangeRepositoryImpl", "Fetched from API")
                val values = remoteResponse.map {
                    ExchangeModel.fromRemoteEntity(it)
                }
                val localEntities = values.map { model ->
                    model.toLocalEntity()
                }
                localEntities.forEach {
                    Log.d("ExchangeRepositoryImpl", "Local entity: $it")
                }
                localDataSource.saveExchangeData(localEntities)
                Log.d("ExchangeRepositoryImpl", "Saved to local database")
                values
            } catch (e: Exception) {
                throw RepositoryError("${e.message}", e)
            }
        }
    }

    override suspend fun getFormattedLastUpdatedDate(): String {
        val lastUpdatedTimestamp = localDataSource.getLastUpdatedTimestamp()?.timestamp ?: return ""
        val lastUpdatedZonedDateTime = ZonedDateTime
            .ofInstant(Instant.ofEpochMilli(lastUpdatedTimestamp), ZoneId.systemDefault())
        val formattedDate = DateTimeFormatter
            .ofLocalizedDateTime(FormatStyle.SHORT)
            .withLocale(Locale.getDefault())
        return lastUpdatedZonedDateTime.format(formattedDate)
    }

    private suspend fun shouldFetchFromApi(): Boolean {
        val lastUpdatedTimestamp = localDataSource.getLastUpdatedTimestamp() ?: return true
        val currentTime = System.currentTimeMillis()
        val timeDifference = currentTime - lastUpdatedTimestamp.timestamp
        return timeDifference >= 60 * 60 * 1000
    }

    private suspend fun fetchFromLocalDatabase(): List<ExchangeModel> {
        val values = localDataSource.getAllExchangeValues()
        if (values.isEmpty()) {
            throw RepositoryError("An unexpected error occurred")
        }
        return values.map { localEntity ->
            ExchangeModel.fromLocalEntity(localEntity)
        }
    }
}