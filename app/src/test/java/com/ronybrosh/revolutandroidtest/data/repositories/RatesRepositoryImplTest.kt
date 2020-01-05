package com.ronybrosh.revolutandroidtest.data.repositories

import com.google.gson.JsonParser
import com.ronybrosh.revolutandroidtest.data.api.RatesApi
import com.ronybrosh.revolutandroidtest.domain.model.Rate
import com.ronybrosh.revolutandroidtest.domain.model.Resource
import com.ronybrosh.revolutandroidtest.domain.repositories.RatesRepository
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class RatesRepositoryImplTest {

    companion object {
        private lateinit var ratesRepository: RatesRepository

        lateinit var ratesApi: RatesApi

        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            ratesApi = mock(RatesApi::class.java)
            ratesRepository = RatesRepositoryImpl(ratesApi)
        }
    }

    /**
     *  Verify that only 2 items emitted
     */
    @Test
    fun getRateList_mockedValidResult_emit2Items() {
        // Arrange
        val testObserver = TestObserver<Resource<List<Rate>>>()

        val jsonElement = JsonParser().parse(javaClass.getResource("/get_rates.json").readText())
        `when`(ratesApi.getRateList()).thenReturn(Single.just(jsonElement))

        // Act
        ratesRepository.getRateList().subscribe(testObserver)
        testObserver.awaitTerminalEvent()

        // Assert
        testObserver.assertValueCount(2)
        testObserver.dispose()
    }

    /**
     *  Verify that Resource.Loading is emitted before Resource.Success
     */
    @Test
    fun getRateList_mockedValidResult_resourceLoadingIsEmittedThenResourceSuccessWithRateList() {
        // Arrange
        val testObserver = TestObserver<Resource<List<Rate>>>()

        val jsonElement = JsonParser().parse(javaClass.getResource("/get_rates.json").readText())
        `when`(ratesApi.getRateList()).thenReturn(Single.just(jsonElement))

        // Act
        ratesRepository.getRateList().subscribe(testObserver)
        testObserver.awaitTerminalEvent()

        // Assert
        assertTrue(testObserver.values()[0] is Resource.Loading)
        assertTrue(testObserver.values()[1] is Resource.Success)

        val result = testObserver.values()[1] as Resource.Success
        assertTrue(result.data.isNotEmpty())

        testObserver.dispose()
    }


    /**
     *  Verify that Resource.Loading is emitted before Resource.Error
     */
    @Test
    fun getRateList_mockedError_resourceLoadingIsEmittedThenResourceError() {
        // Arrange
        val testObserver = TestObserver<Resource<List<Rate>>>()

        `when`(ratesApi.getRateList()).thenReturn(Single.error(Exception()))

        // Act
        ratesRepository.getRateList().subscribe(testObserver)
        testObserver.awaitTerminalEvent()

        // Assert
        assertTrue(testObserver.values()[0] is Resource.Loading)
        assertTrue(testObserver.values()[1] is Resource.Error)

        testObserver.dispose()
    }
}