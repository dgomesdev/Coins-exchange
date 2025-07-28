package com.dgomesdev.exchangeapp.data.local

import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
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
import kotlin.test.assertFailsWith

@RunWith(MockitoJUnitRunner::class)
class ExchangeLocalDataSourceImplTest {

    @Mock
    private lateinit var mockDao: ExchangeDao

    private val mockEntity = ExchangeLocalEntity("USD-BRL", "1.0", 1.0, "USDBRL")

    @Test
    fun `getAll   success   Empty DB`() {
        runTest {
            // Given
            mockDao = mock<ExchangeDao> {
                on { getAll() } doReturn flowOf(emptyList())
            }
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            //  When
            val result = localDataSource.getAll().single()

            // Then
            assertEquals(emptyList<ExchangeLocalEntity>(), result)
        }
    }

    @Test
    fun `getAll   success   Non empty DB`() {
        // Verify that getAll() returns a Flow containing a list of all ExchangeLocalEntity objects stored in the database.
        runTest {
            // Given
            mockDao = mock<ExchangeDao> {
                on { getAll() } doReturn flowOf(listOf(mockEntity))
            }
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            //  When
            val result = localDataSource.getAll().single()

            // Then
            assertEquals(listOf(mockEntity), result)
        }
    }

    @Test
    fun `getAll   error   Database exception`() {
        // Verify that getAll() correctly propagates any exceptions thrown by the underlying exchangeDao.getAll() method (e.g., database connection error).
        runTest {
            // Given
            mockDao = mock<ExchangeDao> {
                on { getAll() } doThrow RuntimeException("Database error")
            }
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            //  When
            val thrown = assertFailsWith<RuntimeException> {
                localDataSource.getAll().collect()
            }

            // Then
            assertEquals("Database error", thrown.message)
        }
    }

    @Test
    fun `save   success   New entity`() {
        // Verify that save() successfully inserts a new ExchangeLocalEntity into the database.
        runTest {
            // Given
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            // When
            localDataSource.save(mockEntity)

            // Then
            // Verify that mockDao.save() was called exactly once with the entityToSave
            verify(mockDao, times(1)).save(mockEntity)
            // You can also use verify(mockDao).save(entityToSave) which defaults to times(1)
        }
    }

    @Test
    fun `save   error   Database exception`() {
        // Verify that save() correctly propagates any exceptions thrown by the underlying exchangeDao.save() method (e.g., database constraint violation, connection error).
        runTest {
            // Given
            val entityToSave = mockEntity
            val errorMessage = "DAO save failed"
            mockDao = mock<ExchangeDao> {
                // Configure mockDao.save() to throw an exception when called with any entity
                onBlocking { save(any()) } doThrow RuntimeException(errorMessage)
            }
            val localDataSource = ExchangeLocalDataSourceImpl(mockDao)

            // When
            val thrown = assertFailsWith<RuntimeException> {
                localDataSource.save(entityToSave)
            }

            // Then
            assertEquals(errorMessage, thrown.message)
            verify(mockDao).save(entityToSave) // Optionally verify it was still called
        }
    }
}