package com.dgomesdev.exchangeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ExchangeLocalEntity::class, LastUpdatedTimestamp::class], version = 2, exportSchema = false)
abstract class ExchangeDatabase : RoomDatabase() {

    abstract fun exchangeDao(): ExchangeDao
}