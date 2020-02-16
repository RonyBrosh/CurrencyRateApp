package com.ronybrosh.currencyrateapp.presentation.util

import java.math.RoundingMode
import java.text.NumberFormat

object RateUtil {
    private val decimalFormat = NumberFormat.getInstance().apply {
        roundingMode = RoundingMode.HALF_UP
        maximumFractionDigits = 2
        minimumFractionDigits = 2
    }

    /**
     * Convert a given amount and currency to other currency
     * The equation is: <amount> * <to currency rate> / <from currency rate>
     *
     * Example:
     * For those given rates:
     * 1 euro
     * 3.87 shekel
     * 0.85 pound
     *
     * The conversion of 2 euro will be:
     * 2 * euro / euro = 2 * 1 / 1 = 2 euro
     * 2 * shekel / euro = 2 * 3.87 / 1 = 7.74 shekel
     * 2 * pound / euro = 2 * 0.85 / 1 = 1.7 pound
     *
     * The conversion of 2 shekel will be:
     * 2 * euro / shekel = 2 * 1 / 3.87 = 0.516 euro
     * 2 * shekel / shekel = 2 * 3.87 / 3.87 = 2 shekel
     * 2 * pound / shekel = 2 * 0.85 / 3.87 = 0.439 pound
     *
     * All parameters should be >= 0 or 0 will return
     *
     * @param amount Float, The amount to convert.
     * @param fromCurrencyRate Float, The given currency rate to convert from.
     * @param toCurrencyRate Float, The currency rate to convert to.
     *
     * @return String, The converted amount to the destination currency in nice format of max 2 digits after decimal point
     * or empty string if the result in 0.
     */
    fun convertRatesToString(
        amount: Float,
        fromCurrencyRate: Float,
        toCurrencyRate: Float
    ): String {
        if (amount == 0F || fromCurrencyRate == 0F || toCurrencyRate == 0F)
            return ""

        val result = amount * toCurrencyRate / fromCurrencyRate
        return decimalFormat.format(result).replace(",", "")
    }

    fun convertStringAmountToFloat(amountAsString: String): Float {
        return amountAsString.replace(",", "").toFloatOrNull() ?: 0F
    }
}