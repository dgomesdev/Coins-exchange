package com.dgomesdev.exchangeapp.data.local

import com.dgomesdev.exchangeapp.domain.Coin
import com.dgomesdev.exchangeapp.domain.ConversionPair
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class ExchangeLocalDataSourceImplTest {

    @Mock
    private lateinit var mockDao: ExchangeDao

    companion object {
        private val MOCK_CONVERSION_PAIR = ConversionPair.USDBRL

        private val MOCK_ENTITY = ExchangeLocalEntity(
            MOCK_CONVERSION_PAIR.name,
            1.0,
            Coin.USD.name
        )
    }

    @Test
    fun `getAll   success   Empty DB`() {
        runTest {
            // Given
            mockDao = mock<ExchangeDao> {
                on { getAllExchangeValues() } doReturn emptyList()
            }
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            //  When
            val result = localDataSource.getAllExchangeValues()

            // Then
            assertEquals(emptyList(), result)
        }
    }

    @Test
    fun `getAll   success   Non empty DB`() {
        runTest {
            // Given
            mockDao = mock<ExchangeDao> {
                on { getAllExchangeValues() } doReturn listOf(MOCK_ENTITY)
            }
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            //  When
            val result = localDataSource.getAllExchangeValues()

            // Then
            assertEquals(listOf(MOCK_ENTITY), result)
        }
    }

    @Test
    fun `getAll   error   Database exception`() {
        runTest {
            // Given
            mockDao = mock<ExchangeDao> {
                on { getAllExchangeValues() } doThrow RuntimeException("Database error")
            }
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            //  When
            val thrown = assertFailsWith<RuntimeException> {
                localDataSource.getAllExchangeValues()
            }

            // Then
            assertEquals("Database error", thrown.message)
        }
    }

    @Test
    fun `save   success   New entity`() {
        runTest {
            // Given
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            // When
            localDataSource.saveExchangeData(listOf(MOCK_ENTITY))

            // Then
            verify(mockDao, times(1)).saveExchange(MOCK_ENTITY)
        }
    }

    @Test
    fun `save   error   Database exception`() {
        runTest {
            // Given
            val entityToSave = MOCK_ENTITY
            val errorMessage = "DAO save failed"
            mockDao = mock<ExchangeDao> {
                on { saveExchange(any()) } doThrow RuntimeException(errorMessage)
            }
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            // When
            val thrown = assertFailsWith<RuntimeException> {
                localDataSource.saveExchangeData(listOf(entityToSave))
            }

            // Then
            assertEquals(errorMessage, thrown.message)
            verify(mockDao).saveExchange(entityToSave)
        }
    }
}