package com.dgomesdev.exchangeapp.domain

import com.dgomesdev.exchangeapp.data.local.ExchangeLocalEntity
import com.dgomesdev.exchangeapp.data.remote.ExchangeRemoteEntity

data class ExchangeModel(
    val conversionPair: ConversionPair,
    val name: String,
    val bid: Double,
    val code: String
) {

    fun toLocalEntity() = ExchangeLocalEntity(
        this.conversionPair.name,
        this.name,
        this.bid,
        this.code
    )

    companion object {
        fun fromRemoteEntity(entity: ExchangeRemoteEntity): ExchangeModel =
             ExchangeModel(
                 ConversionPair.fromEntity(entity.key),
                entity.value.name,
                entity.value.bid,
                 entity.value.code
            )

        fun fromLocalEntity(entity: ExchangeLocalEntity): ExchangeModel =
            ExchangeModel(
                ConversionPair.fromEntity(entity.conversionPair),
                entity.name,
                entity.bid,
                entity.code
            )
    }
}