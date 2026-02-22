package com.dgomesdev.exchangeapp.data.repository

import com.dgomesdev.exchangeapp.data.local.ExchangeLocalDataSource
import com.dgomesdev.exchangeapp.data.local.ExchangeLocalEntity
import com.dgomesdev.exchangeapp.data.local.LastUpdatedTimestamp
import com.dgomesdev.exchangeapp.data.remote.ExchangeRemoteDataSource
import com.dgomesdev.exchangeapp.data.remote.ExchangeResponse
import com.dgomesdev.exchangeapp.data.remote.ExchangeValue
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import com.dgomesdev.exchangeapp.domain.InvalidCoinsException
import com.dgomesdev.exchangeapp.domain.RepositoryError
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import kotlin.test.assertEquals

@RunWith(MockitoJUnitRunner::class)
class ExchangeRepositoryImplTest {

    @Mock
    private lateinit var mockRemoteDataSource: ExchangeRemoteDataSource

    @Mock
    private lateinit var mockLocalDataSource: ExchangeLocalDataSource

    companion object {
        private val MOCK_CONVERSION_PAIR = ConversionPair.USDBRL

        private val MOCK_MODEL = ExchangeModel(
            conversionPair = MOCK_CONVERSION_PAIR,
            bid = 5.7276,
            code = Coin.USD.name,
        )

        private val MOCK_EXCHANGE_VALUE = ExchangeValue(
            code = "USD",
            codein = "BRL",
            name = "Dólar Americano/Real Brasileiro",
            high = "5.734",
            low = "5.7279",
            varBid = "-0.0054",
            pctChange = "-0.09",
            bid = "5.7276",
            ask = "5.7282",
            timestamp = "1618315045",
            createDate = "2021-04-13 08:57:27"
        )

        private val MOCK_EXCHANGE_RESPONSE: ExchangeResponse = mapOf(
            MOCK_CONVERSION_PAIR.name to MOCK_EXCHANGE_VALUE
        )

        private val MOCK_ENTITY = ExchangeLocalEntity(
            MOCK_CONVERSION_PAIR.name,
            5.7276,
            Coin.USD.name
        )
    }

        @Test
        fun `Empty conversionPairs list throws IllegalArgumentException`() {
            runTest {
                // Given
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {}
                mockLocalDataSource = mock<ExchangeLocalDataSource> {}
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val result = repository.getValues(emptyList())

                // Then
                assertEquals(
                    Result.failure(
                        InvalidCoinsException("Coins list can't be empty")
                    ),
                    result
                )
            }
        }

        @Test
        fun `Successful local fetch`() {
            runTest {
                // Given
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    on {
                        getLastUpdatedTimestamp()
                    } doReturn LastUpdatedTimestamp(timestamp = System.currentTimeMillis())
                    on {
                        getAllExchangeValues()
                    } doReturn listOf(MOCK_ENTITY)
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val result = repository.getValues(listOf(MOCK_CONVERSION_PAIR))

                // Then
                assertEquals(Result.success(listOf(MOCK_MODEL)), result)
            }
        }

        @Test
        fun `Successful remote fetch and save`() {
            runTest {
                // Given
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    on {
                        getLastUpdatedTimestamp()
                    } doReturn LastUpdatedTimestamp(timestamp = System.currentTimeMillis() - 61 * 60 * 1000)
                    on {
                        saveExchangeData(listOf(MOCK_ENTITY))
                    } doReturn Unit
                }
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {
                    on {
                        getExchangeValues(MOCK_CONVERSION_PAIR.coins)
                    } doReturn Result.success(MOCK_EXCHANGE_RESPONSE)
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val result = repository.getValues(listOf(MOCK_CONVERSION_PAIR))

                // Then
                assertEquals(Result.success(listOf(MOCK_MODEL)), result)
            }
        }

        @Test
        fun `Remote fetch fails local fetch succeeds`() {
            runTest {
                // Given
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    on {
                        getLastUpdatedTimestamp()
                    } doReturn LastUpdatedTimestamp(timestamp = System.currentTimeMillis() - 61 * 60 * 1000)
                    on {
                        getAllExchangeValues()
                    } doReturn listOf(MOCK_ENTITY)
                }
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {
                    on {
                        getExchangeValues(MOCK_CONVERSION_PAIR.coins)
                    } doReturn Result.failure(RuntimeException("Remote fetch failed"))
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val result = repository.getValues(listOf(MOCK_CONVERSION_PAIR))

                // Then
                assertEquals(Result.success(listOf(MOCK_MODEL)), result)
            }
        }

        @Test
        fun `Remote fetch fails local fetch fails return error result`() {
            runTest {
                // Given
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    on {
                        getLastUpdatedTimestamp()
                    } doReturn null
                    on {
                        getAllExchangeValues()
                    } doReturn emptyList()
                }
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {
                    on {
                        getExchangeValues(MOCK_CONVERSION_PAIR.coins)
                    } doReturn Result.failure(RuntimeException("Remote fetch failed"))
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val result = repository.getValues(listOf(MOCK_CONVERSION_PAIR))

                // Then
                assertEquals(
                    Result.failure(
                        RepositoryError("An unexpected error occurred")
                    ),
                    result
                )
            }
        }

        @Test
        fun `Error during localDataSource save operation`() {
            runTest {
                // Given
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    on {
                        getLastUpdatedTimestamp()
                    } doReturn LastUpdatedTimestamp(timestamp = System.currentTimeMillis() - 61 * 60 * 1000)
                    on {
                        saveExchangeData(listOf(MOCK_ENTITY))
                    } doThrow RuntimeException("Local save failed")
                }
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {
                    on {
                        getExchangeValues(MOCK_CONVERSION_PAIR.coins)
                    } doReturn Result.success(MOCK_EXCHANGE_RESPONSE)
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val result = repository.getValues(listOf(MOCK_CONVERSION_PAIR))


                // Then
                assertEquals(
                    Result.failure(
                        RepositoryError("Local save failed")
                    ),
                    result
                )
            }
        }
}
