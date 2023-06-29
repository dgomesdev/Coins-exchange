package com.dgomesdev.exchangeapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dgomesdev.exchangeapp.domain.model.ExchangeValues
import kotlinx.coroutines.flow.Flow

@Dao
interface ExchangeDao {

    @Query("SELECT * FROM coin_table")
    fun getAll(): Flow<List<ExchangeValues>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(exchangeValues: ExchangeValues)
}