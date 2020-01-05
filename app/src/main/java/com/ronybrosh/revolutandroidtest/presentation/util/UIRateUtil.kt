package com.ronybrosh.revolutandroidtest.presentation.util

import android.content.res.Resources
import android.content.res.TypedArray
import com.ronybrosh.revolutandroidtest.R
import com.ronybrosh.revolutandroidtest.domain.model.Rate
import com.ronybrosh.revolutandroidtest.presentation.features.rates.model.UIRate
import java.util.*

class UIRateUtil(private val resources: Resources) {
    fun createNewUpdatedList(
        oldList: List<UIRate>?,
        newList: List<Rate>,
        amount: Float,
        currencyRate: Float
    ): List<UIRate> {
        // 1. If old list is empty create a new list
        if (oldList.isNullOrEmpty()) {
            return createNewUIRateList(
                amount = amount,
                currencyRate = currencyRate,
                rateList = newList
            )
        }

        // 2. If old list is not empty, update the list and keep the items order
        return updateNewUIRateListAndKeepItemsOrderAccordingToOldList(
            amount = amount,
            currencyRate = currencyRate,
            rateList = newList,
            oldList = oldList
        )
    }

    fun moveToTopOfList(fromIndex: Int, oldList: List<UIRate>?): List<UIRate> {
        if (oldList == null)
            return emptyList()

        val newList = mutableListOf<UIRate>()
        newList.addAll(oldList)

        val itemToMove = newList.removeAt(fromIndex)
        newList.add(0, itemToMove)

        return newList
    }

    private fun createNewUIRateList(
        amount: Float,
        currencyRate: Float,
        rateList: List<Rate>
    ): List<UIRate> {
        if (rateList.isEmpty())
            return emptyList()

        val result: ArrayList<UIRate> = arrayListOf()

        val iconsArray: TypedArray = resources.obtainTypedArray(R.array.rate_icons)
        val namesArray: Array<String> = resources.getStringArray(R.array.rate_names)
        val currencySet: Array<String> = resources.getStringArray(R.array.rate_currencies)

        rateList.forEach { rate ->
            val index = currencySet.indexOfFirst {
                it == rate.currency
            }
            if (index == -1)
                return@forEach

            result.add(
                UIRate(
                    iconResourceId = iconsArray.getResourceId(index, 0),
                    currencyCode = currencySet[index],
                    currencyName = namesArray[index],
                    convertedAmount = RateUtil.convertRatesToString(
                        amount = amount,
                        fromCurrencyRate = currencyRate,
                        toCurrencyRate = rate.rate
                    ),
                    rate = rate.rate
                )
            )
        }
        iconsArray.recycle()
        return result.toList()
    }

    private fun updateNewUIRateListAndKeepItemsOrderAccordingToOldList(
        amount: Float,
        currencyRate: Float,
        oldList: List<UIRate>,
        rateList: List<Rate>
    ): List<UIRate> {
        val newList = mutableListOf<UIRate>()
        oldList.forEach { nextUIRate ->
            // Find matching rate item
            val matchingRate = rateList.firstOrNull { rate ->
                rate.currency == nextUIRate.currencyCode
            }

            if (matchingRate != null) {
                // Create a new item
                val newUIRate = UIRate(
                    iconResourceId = nextUIRate.iconResourceId,
                    currencyCode = nextUIRate.currencyCode,
                    currencyName = nextUIRate.currencyName,
                    convertedAmount = RateUtil.convertRatesToString(
                        amount = amount,
                        fromCurrencyRate = currencyRate,
                        toCurrencyRate = matchingRate.rate
                    ),
                    rate = matchingRate.rate
                )
                // Add the new item to the new list
                newList.add(newUIRate)
            }
        }
        return newList
    }
}