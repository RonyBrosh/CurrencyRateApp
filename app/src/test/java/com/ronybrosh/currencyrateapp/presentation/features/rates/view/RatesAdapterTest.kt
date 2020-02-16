package com.ronybrosh.currencyrateapp.presentation.features.rates.view

import com.ronybrosh.currencyrateapp.presentation.features.rates.adapter.RatesDiffCallback
import com.ronybrosh.currencyrateapp.presentation.features.rates.model.UIRate
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class RatesAdapterTest {
    /**
     * Verify that areItemsTheSame return correct result
     */
    @Test
    fun areItemsTheSame_sameItems_true() {
        // Arrange
        val item1 = UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "1",
            rate = 1F
        )

        val item2 = UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "2",
            rate = 1F
        )
        val rateDiffCallback = RatesDiffCallback(listOf(item1), listOf(item2))

        // Act
        val result = rateDiffCallback.areItemsTheSame(0, 0)

        // Assert
        assertTrue(result)
    }

    /**
     * Verify that areItemsTheSame return correct result
     */
    @Test
    fun areItemsTheSame_differentItems_false() {
        // Arrange
        val item1 = UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "1",
            rate = 1F
        )

        val item2 = UIRate(
            iconResourceId = 0,
            currencyCode = "AUD",
            currencyName = "Euro",
            convertedAmount = "2",
            rate = 1F
        )
        val rateDiffCallback = RatesDiffCallback(listOf(item1), listOf(item2))

        // Act
        val result = rateDiffCallback.areItemsTheSame(0, 0)

        // Assert
        assertFalse(result)
    }

    /**
     * Verify that areContentsTheSame return correct result
     */
    @Test
    fun areContentsTheSame_sameItems_true() {
        // Arrange
        val item1 = UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "1",
            rate = 1F
        )

        val item2 = UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "1",
            rate = 1F
        )
        val rateDiffCallback = RatesDiffCallback(listOf(item1), listOf(item2))

        // Act
        val result = rateDiffCallback.areContentsTheSame(0, 0)

        // Assert
        assertTrue(result)
    }

    /**
     * Verify that areContentsTheSame return correct result
     */
    @Test
    fun areContentsTheSame_differentItems_false() {
        // Arrange
        val item1 = UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "1",
            rate = 1F
        )

        val item2 = UIRate(
            iconResourceId = 0,
            currencyCode = "AUD",
            currencyName = "Euro",
            convertedAmount = "2",
            rate = 1F
        )
        val rateDiffCallback = RatesDiffCallback(listOf(item1), listOf(item2))

        // Act
        val result = rateDiffCallback.areContentsTheSame(0, 0)

        // Assert
        assertFalse(result)
    }
}