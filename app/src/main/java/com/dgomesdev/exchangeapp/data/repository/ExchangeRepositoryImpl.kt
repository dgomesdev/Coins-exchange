package com.dgomesdev.exchangeapp.data.repository

import com.dgomesdev.exchangeapp.data.local.ExchangeLocalDataSource
import com.dgomesdev.exchangeapp.data.remote.ExchangeRemoteDataSource
import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExchangeRepositoryImpl @Inject constructor(
    private val remoteDataSource: ExchangeRemoteDataSource,
    private val localDataSource: ExchangeLocalDataSource
) : ExchangeRepository {
    override fun getValues(conversionPairs: List<ConversionPair>): Flow<List<ExchangeModel>> {
        if (conversionPairs.isEmpty()) {
            throw IllegalArgumentException("Coins list can't be empty")
        }

        val coinsList: String = conversionPairs.joinToString(separator = ",") { it.pair }

        val remoteFetchAndSaveFlow = flow {
            val remoteResult = remoteDataSource.getExchangeValues(coinsList).map { entity ->
                ExchangeModel.fromRemoteEntity(entity)
            }
            remoteResult.forEach { exchangeModel ->
                localDataSource.save(exchangeModel.toLocalEntity())
            }
            emit(remoteResult)
        }

        return remoteFetchAndSaveFlow.catch { error ->
            val currentLocalEntities = localDataSource.getAll().firstOrNull()
            if (currentLocalEntities != null) {
                val localResult = currentLocalEntities.map { entity ->
                    ExchangeModel.fromLocalEntity(entity)
                }
                emit(localResult)
            } else {
                throw RuntimeException("Failed to fetch from remote, and no local data found.")
            }
        }
    }
}