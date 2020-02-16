package com.ronybrosh.currencyrateapp.presentation.features.common.model

data class UIChangePayload<out T>(
    val oldData: T,
    val newData: T
)

fun <T> convertToSingleUIChangePayload(payloads: List<UIChangePayload<T>>): UIChangePayload<T> {
    val firstChange = payloads.first()
    val lastChange = payloads.last()
    return UIChangePayload(firstChange.oldData, lastChange.newData)
}
