package com.ronybrosh.revolutandroidtest.presentation.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource

class RateUtilTest {
    companion object {
        @JvmStatic
        fun amountPairList(): List<Pair<String, Float>> {
            return listOf(
                Pair("", 0F),
                Pair("0", 0F),
                Pair("1", 1F),
                Pair("10", 10F),
                Pair("1.17", 1.17F)
            )
        }
    }

    /**
     * Convert 2 euro to pound return correct conversion
     */
    @Test
    fun convertRates_validInput_validResult() {
        // Arrange
        val amount = 2F
        val fromCurrencyRate = 1F
        val toCurrencyRate = 0.85F

        // Act
        val result = RateUtil.convertRatesToString(
            amount = amount,
            fromCurrencyRate = fromCurrencyRate,
            toCurrencyRate = toCurrencyRate
        )

        // Assert
        assertEquals("1.70", result)
    }

    /**
     * Convert amount that return whole number
     */
    @Test
    fun convertRates_mockInput_wholeNumberResult() {
        // Arrange
        val amount = 2F
        val fromCurrencyRate = 1F
        val toCurrencyRate = 0.5F

        // Act
        val result = RateUtil.convertRatesToString(
            amount = amount,
            fromCurrencyRate = fromCurrencyRate,
            toCurrencyRate = toCurrencyRate
        )

        // Assert
        assertEquals("1.00", result)
    }

    /**
     * Convert 0 euro to pound return empty string
     */
    @Test
    fun convertRates_zeroAmount_zero() {
        // Arrange
        val amount = 0F
        val fromCurrencyRate = 1F
        val toCurrencyRate = 0.85F

        // Act
        val result = RateUtil.convertRatesToString(
            amount = amount,
            fromCurrencyRate = fromCurrencyRate,
            toCurrencyRate = toCurrencyRate
        )

        // Assert
        assertEquals("", result)
    }

    /**
     * Convert 2 euro where from currency is 0 return empty string
     */
    @Test
    fun convertRates_zeroFromCurrencyRate_zero() {
        // Arrange
        val amount = 2F
        val fromCurrencyRate = 0F
        val toCurrencyRate = 0.85F

        // Act
        val result = RateUtil.convertRatesToString(
            amount = amount,
            fromCurrencyRate = fromCurrencyRate,
            toCurrencyRate = toCurrencyRate
        )

        // Assert
        assertEquals("", result)
    }

    /**
     * Convert 2 euro where to currency is 0 return empty string
     */
    @Test
    fun convertRates_zeroToCurrencyRate_zero() {
        // Arrange
        val amount = 2F
        val fromCurrencyRate = 1F
        val toCurrencyRate = 0F

        // Act
        val result = RateUtil.convertRatesToString(
            amount = amount,
            fromCurrencyRate = fromCurrencyRate,
            toCurrencyRate = toCurrencyRate
        )

        // Assert
        assertEquals("", result)
    }

    /**
     * Convert 2 euro where to currency is 0 return 0
     */
    @Test
    fun convertRates__zero() {
        // Arrange
        val amount = 1F
        val fromCurrencyRate = 2F
        val toCurrencyRate = 1F

        // Act
        val result = RateUtil.convertRatesToString(
            amount = amount,
            fromCurrencyRate = fromCurrencyRate,
            toCurrencyRate = toCurrencyRate
        )

        // Assert
        assertEquals("0.50", result)
    }

    /**
     * Convert amount as string to amount as float
     */
    @ParameterizedTest
    @MethodSource("amountPairList")
    fun convertStringAmountToFloat_mockedValues_correctConversion(pair: Pair<String, Float>) {
        // Arrange

        // Act
        val result = RateUtil.convertStringAmountToFloat(pair.first)

        // Assert
        assertEquals(pair.second, result)
    }
}