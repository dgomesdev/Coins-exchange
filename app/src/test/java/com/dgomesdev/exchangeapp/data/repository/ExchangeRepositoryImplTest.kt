package com.dgomesdev.exchangeapp.data.repository

import com.dgomesdev.exchangeapp.data.local.ExchangeLocalDataSource
import com.dgomesdev.exchangeapp.data.local.ExchangeLocalEntity
import com.dgomesdev.exchangeapp.data.remote.ExchangeRemoteDataSource
import com.dgomesdev.exchangeapp.data.remote.ExchangeResponse
import com.dgomesdev.exchangeapp.data.remote.ExchangeValue
import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ConversionPair
import com.dgomesdev.exchangeapp.domain.ExchangeModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class ExchangeRepositoryImplTest {

    @Mock
    private lateinit var mockRemoteDataSource: ExchangeRemoteDataSource

    @Mock
    private lateinit var mockLocalDataSource: ExchangeLocalDataSource

    private val mockConversionPair = ConversionPair.USDBRL

    private val mockModel = ExchangeModel(
        conversionPair = mockConversionPair,
        bid = 5.7276,
        code = Coin.USD.name,
    )

    private val mockExchangeValue = ExchangeValue(
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

    private val mockExchangeResponse: ExchangeResponse = mapOf(
        mockConversionPair.name to mockExchangeValue
    )

    private val mockEntity = ExchangeLocalEntity(
        mockConversionPair.name,
        5.7276,
        Coin.USD.name
    )

        @Test
        fun `Empty conversionPairs list throws IllegalArgumentException`() {
            runTest {
                // Given
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {}
                mockLocalDataSource = mock<ExchangeLocalDataSource> {}
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val thrown = assertFailsWith<IllegalArgumentException> {
                    repository.getValues(emptyList()).collect()
                }

                // Then
                assertEquals("Coins list can't be empty", thrown.message)
            }
        }

        @Test
        fun `Successful remote fetch and save`() {
            runTest {
                // Given
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {
                    onBlocking {
                        getExchangeValues(mockConversionPair.coins)
                    } doReturn mockExchangeResponse
                }
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    onBlocking { save(any<ExchangeLocalEntity>()) } doReturn Unit
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val result = repository.getValues(listOf(mockConversionPair)).firstOrNull()

                // Then
                assertEquals(listOf(mockModel), result)
            }
        }

        @Test
        fun `Remote fetch fails  local data exists and is emitted`() {
            runTest {
                // Given
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {
                    onBlocking {
                        getExchangeValues(mockConversionPair.coins)
                    } doThrow RuntimeException("Remote fetch failed")
                }
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    onBlocking { getAll() } doReturn flowOf(listOf(mockEntity))
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val result = repository.getValues(listOf(mockConversionPair)).firstOrNull()

                // Then
                assertEquals(listOf(mockModel), result)
            }
        }

        @Test
        fun `Remote fetch fails  local data is empty  throws RuntimeException`() {
            runTest {
                // Given
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {
                    onBlocking {
                        getExchangeValues(mockConversionPair.coins)
                    } doThrow RuntimeException("Remote fetch failed")
                }
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    onBlocking { getAll() } doReturn flowOf(emptyList())
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val thrown = assertFailsWith<RuntimeException> {
                    repository.getValues(listOf(mockConversionPair)).firstOrNull()
                }

                // Then
                assertEquals(
                    "Failed to fetch from remote, and no local data found.",
                    thrown.message
                )
            }
        }

        @Test
        fun `Error during localDataSource save operation`() {
            runTest {
                // Given
                mockRemoteDataSource = mock<ExchangeRemoteDataSource> {
                    onBlocking {
                        getExchangeValues(mockConversionPair.coins)
                    } doReturn mockExchangeResponse
                }
                mockLocalDataSource = mock<ExchangeLocalDataSource> {
                    onBlocking {
                        save(any<ExchangeLocalEntity>())
                    } doThrow RuntimeException("Local save failed")
                    onBlocking { getAll() } doReturn flowOf(emptyList())
                }
                val repository = ExchangeRepositoryImpl(mockRemoteDataSource, mockLocalDataSource)

                // When
                val thrown = assertFailsWith<RuntimeException> {
                    repository.getValues(listOf(mockConversionPair)).firstOrNull()
                }

                // Then
                assertEquals(
                    "Failed to fetch from remote, and no local data found.",
                    thrown.message
                )
            }
        }
}