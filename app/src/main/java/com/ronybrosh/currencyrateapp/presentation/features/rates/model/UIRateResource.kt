package com.ronybrosh.currencyrateapp.presentation.features.rates.model

data class UIRateResource(
    val isScrollToTop: Boolean = false,
    val content: List<UIRate>
)