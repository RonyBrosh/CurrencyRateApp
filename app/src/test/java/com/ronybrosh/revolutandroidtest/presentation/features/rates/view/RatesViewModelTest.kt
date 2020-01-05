package com.ronybrosh.revolutandroidtest.presentation.features.rates.view

import androidx.lifecycle.Observer
import com.ronybrosh.revolutandroidtest.TestContentUtil
import com.ronybrosh.revolutandroidtest.domain.model.Resource
import com.ronybrosh.revolutandroidtest.domain.repositories.RatesRepository
import com.ronybrosh.revolutandroidtest.presentation.features.common.model.UIError
import com.ronybrosh.revolutandroidtest.presentation.features.rates.model.UIRate
import com.ronybrosh.revolutandroidtest.presentation.features.rates.model.UIRateResource
import com.ronybrosh.revolutandroidtest.presentation.util.UIRateUtil
import com.ronybrosh.revolutandroidtest.util.InstantExecutorExtension
import io.reactivex.Observable
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@ExtendWith(InstantExecutorExtension::class)
class RatesViewModelTest {

    companion object {
        @JvmStatic
        @BeforeAll
        fun beforeAll() {
            RxJavaPlugins.setIoSchedulerHandler { Schedulers.trampoline() }
            RxAndroidPlugins.setInitMainThreadSchedulerHandler { Schedulers.trampoline() }
        }

        @JvmStatic
        @AfterAll
        fun afterAll() {
            RxJavaPlugins.reset()
            RxAndroidPlugins.reset()
        }

        inline fun <reified T> anyNonNull(): T = any<T>(T::class.java)
    }

    private lateinit var viewModel: RatesViewModel

    // Dependencies
    @Mock
    lateinit var ratesRepository: RatesRepository

    @Mock
    lateinit var uiRateUtil: UIRateUtil

    // Observers
    @Mock
    lateinit var loadingObserver: Observer<Boolean>

    @Mock
    lateinit var errorObserver: Observer<UIError?>

    @Mock
    lateinit var uiRateListObserver: Observer<UIRateResource>

    // Captors
    @Captor
    lateinit var loadingCaptor: ArgumentCaptor<Boolean>

    @Captor
    lateinit var errorCaptor: ArgumentCaptor<UIError?>

    @Captor
    lateinit var uiRateListCaptor: ArgumentCaptor<UIRateResource>

    @BeforeEach
    fun beforeEach() {
        MockitoAnnotations.initMocks(this)
        viewModel = RatesViewModel(ratesRepository, uiRateUtil)
    }

    /**
     * Verify that fetching rates is called every 1 second
     * For a time range of 5 seconds the events order will be:
     * 1. Loading true
     * 2. Loading false
     * 3. UIRate list result
     *
     * 4. Loading true
     * 5. Loading false
     * 6. UIRate list result
     *
     * etc..
     * Here we don't care for the events as we just want to see that we get at least 5 results (the first result will be immediately)
     */
    @Test
    fun repositoryGetRateList_OneSecondInterval_getCalled() {
        // Arrange
        val latch = CountDownLatch(1)
        `when`(ratesRepository.getRateList()).thenReturn(Observable.just(Resource.Success(listOf())))

        // Act
        viewModel.getResult().observeForever(uiRateListObserver)
        latch.await(5, TimeUnit.SECONDS)

        // Assert
        verify(uiRateListObserver, atLeast(5)).onChanged(uiRateListCaptor.capture())
    }

    /**
     * Observer and verify that for valid result, Loading live data is being triggered and then Rate list live data is triggered
     * The order will be:
     * 1. Loading true
     * 2. Loading false
     * 3. UIRate list result
     */
    @Test
    fun loadingLiveDataAndUIRateListLiveData_validResult_triggered() {
        // Arrange
        val inOrder = inOrder(loadingObserver, uiRateListObserver)

        `when`(ratesRepository.getRateList()).thenReturn(
            Observable.just(
                Resource.Loading(),
                Resource.Success(listOf())
            )
        )

        // Act
        viewModel.getLoading().observeForever(loadingObserver)
        viewModel.getResult().observeForever(uiRateListObserver)

        // Assert
        inOrder.verify(loadingObserver, times(2)).onChanged(loadingCaptor.capture())
        inOrder.verify(uiRateListObserver, times(1)).onChanged(uiRateListCaptor.capture())
        assertTrue(loadingCaptor.allValues[0])
        assertFalse(loadingCaptor.allValues[1])
        assertNotNull(uiRateListCaptor.allValues[0])
    }

    /**
     * Observe and verify that Rate list live data return a list of UIRate
     */
    @Test
    fun uiRateResultLiveData_validResult_triggeredWithUIRateList() {
        // Arrange
        `when`(ratesRepository.getRateList()).thenReturn(
            Observable.just(Resource.Success(TestContentUtil.rateList_1))
        )
        `when`(
            uiRateUtil.createNewUpdatedList(
                any(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull()
            )
        ).thenReturn(TestContentUtil.uiRateList_1)

        // Act
        viewModel.getResult().observeForever(uiRateListObserver)

        // Assert
        verify(uiRateListObserver).onChanged(uiRateListCaptor.capture())
        assertTrue(uiRateListCaptor.allValues[0].content.isNotEmpty())
    }

    /**
     * Observer and verify that Error live data is being triggered for invalid result
     */
    @Test
    fun errorLiveData_invalidResult_triggered() {
        // Arrange
        `when`(ratesRepository.getRateList()).thenReturn(
            Observable.just(Resource.Error(Throwable()))
        )

        // Act
        viewModel.getError().observeForever(errorObserver)
        viewModel.getResult().observeForever(uiRateListObserver)

        // Assert
        verify(errorObserver).onChanged(errorCaptor.capture())
    }

    /**
     * Verify that UIRate list is being updated
     *
     * uiRateUtil.convertRateListToUIRateList should be called only once
     */
    @Test
    fun uiRateResultLiveData_updatedResult_triggeredWithUpdatedUIRateList() {
        // Arrange
        val latch = CountDownLatch(1)

        `when`(ratesRepository.getRateList())
            .thenReturn(Observable.just(Resource.Success(TestContentUtil.rateList_1)))
            .thenReturn(Observable.just(Resource.Success(TestContentUtil.rateList_2)))

        `when`(
            uiRateUtil.createNewUpdatedList(
                any(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull()
            )
        )
            .thenReturn(TestContentUtil.uiRateList_1)
            .thenReturn(TestContentUtil.uiRateList_2)

        // Act
        val liveData = viewModel.getResult()
        liveData.observeForever(uiRateListObserver)
        latch.await(1500, TimeUnit.MILLISECONDS)

        // Assert

        // Verify that we have 2 invocations to test the update
        verify(uiRateListObserver, times(2)).onChanged(uiRateListCaptor.capture())

        // Verify that repository getRateList is called 2 times
        verify(ratesRepository, times(2)).getRateList()

        // Verify that createNewUpdatedList is called only once (for the very first invocation)
        verify(uiRateUtil, times(1)).createNewUpdatedList(
            anyNonNull(),
            anyNonNull(),
            anyNonNull(),
            anyNonNull()
        )

        // Verify that the values are correct
        TestContentUtil.uiRateList_1.forEachIndexed { index, nextControl ->
            val nextResult = uiRateListCaptor.allValues[0].content[index]
            assertNotNull(nextResult.iconResourceId)
            assertEquals(nextControl.currencyCode, nextResult.currencyCode)
            assertEquals(nextControl.currencyName, nextResult.currencyName)
            assertEquals(nextControl.convertedAmount, nextResult.convertedAmount)
            assertEquals(nextControl.rate, nextResult.rate)
        }

        TestContentUtil.uiRateList_2.forEachIndexed { index, nextControl ->
            val nextResult = uiRateListCaptor.allValues[1].content[index]
            assertNotNull(nextResult.iconResourceId)
            assertEquals(nextControl.currencyCode, nextResult.currencyCode)
            assertEquals(nextControl.currencyName, nextResult.currencyName)
            assertEquals(nextControl.convertedAmount, nextResult.convertedAmount)
            assertEquals(nextControl.rate, nextResult.rate)
        }
    }

    /**
     * Verify that updating the rates conversion update UIRate list
     * The conversion of 2 euro will be:
     * 2 * pound / euro = 2 * 0.85 / 1 = 1.7 pound
     */
    @Test
    fun updateRatesConversion_newAmount_updatedUIRateList() {
        // Arrange
        `when`(ratesRepository.getRateList())
            .thenReturn(Observable.just(Resource.Success(TestContentUtil.rateList_1)))

        `when`(
            uiRateUtil.createNewUpdatedList(
                any(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull()
            )
        ).thenReturn(TestContentUtil.uiRateList_1)

        // Act
        viewModel.getResult().observeForever(uiRateListObserver)
        viewModel.updateRatesConversion("2", 1F)

        // Assert
        // Verify that we have 2 invocations to test the update
        verify(uiRateListObserver, times(2)).onChanged(uiRateListCaptor.capture())

        // Verify that the values are correct
        TestContentUtil.uiRateList_1_conversion_for_2.forEachIndexed { index, nextControl ->
            val nextResult = uiRateListCaptor.allValues[1].content[index]
            assertNotNull(nextResult.iconResourceId)
            assertEquals(nextControl.currencyCode, nextResult.currencyCode)
            assertEquals(nextControl.currencyName, nextResult.currencyName)
            assertEquals(nextControl.convertedAmount, nextResult.convertedAmount)
            assertEquals(nextControl.rate, nextResult.rate)
        }
    }

    /**
     * Verify that updating item index top top of list (index 0) shift other item down the list
     */
    @Test
    fun moveToTopOfList_mockedList_updatedUIRateList() {
        // Arrange
        val oldList = TestContentUtil.uiRateList_1
        val lastItemIndex = oldList.size - 1

        `when`(ratesRepository.getRateList())
            .thenReturn(Observable.just(Resource.Success(TestContentUtil.rateList_1)))

        `when`(
            uiRateUtil.createNewUpdatedList(
                any(),
                anyNonNull(),
                anyNonNull(),
                anyNonNull()
            )
        ).thenReturn(TestContentUtil.uiRateList_1)

        `when`(
            uiRateUtil.moveToTopOfList(
                anyNonNull(),
                anyNonNull()
            )
        ).thenReturn(TestContentUtil.uiRateList_1_move_index_2_to_top)

        // Act
        viewModel.getResult().observeForever(uiRateListObserver)
        viewModel.moveToTopOfList(lastItemIndex)

        // Assert
        // Verify that we have 1 invocation to test the update
        verify(uiRateListObserver, times(2)).onChanged(uiRateListCaptor.capture())
        val result: List<UIRate> = uiRateListCaptor.allValues[1].content

        // Verify that the order is correct
        assertNotNull(result)
        assertNotEquals(oldList, result)
        assertEquals(oldList[lastItemIndex].currencyCode, result[0].currencyCode)
        assertEquals(oldList[lastItemIndex].currencyName, result[0].currencyName)
        assertEquals(oldList[lastItemIndex].convertedAmount, result[0].convertedAmount)
        assertEquals(oldList[lastItemIndex].rate, result[0].rate)
    }
}