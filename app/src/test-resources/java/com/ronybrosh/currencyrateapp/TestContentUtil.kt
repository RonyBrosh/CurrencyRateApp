package com.ronybrosh.currencyrateapp

import com.ronybrosh.currencyrateapp.domain.model.Rate
import com.ronybrosh.currencyrateapp.presentation.features.rates.model.UIRate

object TestContentUtil {

    val rateList_1 = listOf(
        Rate(
            currency = "EUR",
            rate = 1F
        ),
        Rate(
            currency = "AUD",
            rate = 1.6143F
        ),
        Rate(
            currency = "BGN",
            rate = 1.9533F
        )
    )

    val uiRateList_1 = listOf(
        UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "1.00",
            rate = 1F
        ),
        UIRate(
            iconResourceId = 0,
            currencyCode = "AUD",
            currencyName = "Australian Dollar",
            convertedAmount = "1.61",
            rate = 1.6143F
        ),
        UIRate(
            iconResourceId = 0,
            currencyCode = "BGN",
            currencyName = "Bulgarian Lev",
            convertedAmount = "1.95",
            rate = 1.9533F
        )
    )

    val rateList_2 = listOf(
        Rate(
            currency = "EUR",
            rate = 1F
        ),
        Rate(
            currency = "AUD",
            rate = 1.7143F
        ),
        Rate(
            currency = "BGN",
            rate = 1.8533F
        )
    )

    val uiRateList_2 = listOf(
        UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "1.00",
            rate = 1F
        ),
        UIRate(
            iconResourceId = 0,
            currencyCode = "AUD",
            currencyName = "Australian Dollar",
            convertedAmount = "1.71",
            rate = 1.7143F
        ),
        UIRate(
            iconResourceId = 0,
            currencyCode = "BGN",
            currencyName = "Bulgarian Lev",
            convertedAmount = "1.85",
            rate = 1.8533F
        )
    )

    val uiRateList_1_conversion_for_2 = listOf(
        UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "2.00",
            rate = 1F
        ),
        UIRate(
            iconResourceId = 0,
            currencyCode = "AUD",
            currencyName = "Australian Dollar",
            convertedAmount = "3.23",
            rate = 1.6143F
        ),
        UIRate(
            iconResourceId = 0,
            currencyCode = "BGN",
            currencyName = "Bulgarian Lev",
            convertedAmount = "3.91",
            rate = 1.9533F
        )
    )

    val uiRateList_1_move_index_2_to_top = listOf(
        UIRate(
            iconResourceId = 0,
            currencyCode = "BGN",
            currencyName = "Bulgarian Lev",
            convertedAmount = "1.95",
            rate = 1.9533F
        ),
        UIRate(
            iconResourceId = 0,
            currencyCode = "EUR",
            currencyName = "Euro",
            convertedAmount = "1.00",
            rate = 1F
        ),
        UIRate(
            iconResourceId = 0,
            currencyCode = "AUD",
            currencyName = "Australian Dollar",
            convertedAmount = "1.61",
            rate = 1.6143F
        )
    )
}
