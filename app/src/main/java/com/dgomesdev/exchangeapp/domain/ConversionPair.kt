package com.dgomesdev.exchangeapp.domain

enum class ConversionPair(val coins: String) {
    BRLUSD("BRL-USD"),
    BRLEUR("BRL-EUR"),
    USDBRL("USD-BRL"),
    USDEUR("USD-EUR"),
    EURBRL("EUR-BRL"),
    EURUSD("EUR-USD");

    companion object {
        fun fromEntity(pairString: String): ConversionPair {
            return entries.find { it.name.equals(pairString, ignoreCase = true) }
                ?: throw IllegalArgumentException(
                    "No Coin enum constant found for conversion pair: $pairString"
                )
        }
    }
}