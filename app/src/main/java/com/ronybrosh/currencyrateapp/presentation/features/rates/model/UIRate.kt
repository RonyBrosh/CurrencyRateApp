package com.ronybrosh.currencyrateapp.presentation.features.rates.model

import androidx.annotation.DrawableRes

data class UIRate(
    @DrawableRes val iconResourceId: Int,
    val currencyCode: String,
    val currencyName: String,
    var convertedAmount: String,
    var rate: Float
)