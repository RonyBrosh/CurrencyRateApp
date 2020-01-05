package com.ronybrosh.revolutandroidtest.presentation.util

import androidx.test.platform.app.InstrumentationRegistry
import com.ronybrosh.revolutandroidtest.TestContentUtil
import com.ronybrosh.revolutandroidtest.presentation.features.rates.model.UIRate
import org.junit.Assert.*
import org.junit.Test

class UIRateUtilTest {

    /**
     * Verify that rate list is being converted to UIRate list correctly
     */
    @Test
    fun convertRateListToUIRateList_validRateList_uiRateList() {
        // Arrange
        val controlList = TestContentUtil.uiRateList_1

        // Act
        val result =
            UIRateUtil(InstrumentationRegistry.getInstrumentation().targetContext.resources).createNewUpdatedList(
                oldList = null,
                amount = 1F,
                currencyRate = 1F,
                newList = TestContentUtil.rateList_1
            )

        // Assert
        result.forEachIndexed { index, nextResult ->
            val nextControl = controlList[index]
            assertNotNull(nextResult.iconResourceId)
            assertEquals(nextControl.currencyCode, nextResult.currencyCode)
            assertEquals(nextControl.currencyName, nextResult.currencyName)
            assertEquals(nextControl.convertedAmount, nextResult.convertedAmount)
            assertEquals(nextControl.rate, nextResult.rate)
        }
    }

    /**
     * Verify that updating the list order work
     */
    @Test
    fun moveToTopOfList_givenIndex_listGetUpdated() {
        // Arrange
        val oldList = TestContentUtil.uiRateList_2
        val lastItemIndex = oldList.size - 1

        // Act
        val result =
            UIRateUtil(InstrumentationRegistry.getInstrumentation().targetContext.resources).moveToTopOfList(
                lastItemIndex,
                oldList
            )

        // Assert
        assertNotNull(result)
        assertNotEquals(oldList, result)
        assertEquals(oldList[lastItemIndex].currencyCode, result[0].currencyCode)
        assertEquals(oldList[lastItemIndex].currencyName, result[0].currencyName)
        assertEquals(oldList[lastItemIndex].convertedAmount, result[0].convertedAmount)
        assertEquals(oldList[lastItemIndex].rate, result[0].rate)
    }
}