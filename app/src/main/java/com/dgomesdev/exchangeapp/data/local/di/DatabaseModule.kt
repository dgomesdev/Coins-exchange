package com.dgomesdev.exchangeapp.data.local.di

import android.app.Application
import androidx.room.Room
import com.dgomesdev.exchangeapp.data.local.ExchangeDao
import com.dgomesdev.exchangeapp.data.local.ExchangeDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideDao(exchangeDatabase: ExchangeDatabase) : ExchangeDao = exchangeDatabase.exchangeDao()

    @Provides
    @Singleton
    fun provideDatabase(application: Application): ExchangeDatabase =
        Room.databaseBuilder(
            application,
            ExchangeDatabase::class.java,
            "exchange_database"
        ).build()
}