package com.dgomesdev.exchangeapp.data.local.di

import android.app.Application
import androidx.room.Room
import com.dgomesdev.exchangeapp.data.local.ExchangeDao
import com.dgomesdev.exchangeapp.data.local.ExchangeDatabase
import com.dgomesdev.exchangeapp.data.local.ExchangeLocalDataSource
import com.dgomesdev.exchangeapp.data.local.ExchangeLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDao(exchangeDatabase: ExchangeDatabase) : ExchangeDao = exchangeDatabase.exchangeDao()

    @Provides
    @Singleton
    fun provideDatabase(application: Application): ExchangeDatabase =
        Room.databaseBuilder(
            application,
            ExchangeDatabase::class.java,
            "exchange_database"
        ).build()

    @Provides
    @Singleton
    fun provideExchangeLocalDataSource(dao: ExchangeDao): ExchangeLocalDataSource = ExchangeLocalDataSourceImpl(dao)
}