package com.dgomesdev.exchangeapp.data.remote.di

import com.dgomesdev.exchangeapp.data.remote.ExchangeApi
import com.dgomesdev.exchangeapp.data.remote.ExchangeRemoteDataSource
import com.dgomesdev.exchangeapp.data.remote.ExchangeRemoteDataSourceImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, gson: Gson): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideExchangeApi(retrofit: Retrofit): ExchangeApi =
        retrofit.create(ExchangeApi::class.java)

    @Provides
    @Singleton
    fun provideExchangeRemoteDataSource(api: ExchangeApi): ExchangeRemoteDataSource =
        ExchangeRemoteDataSourceImpl(api)
}