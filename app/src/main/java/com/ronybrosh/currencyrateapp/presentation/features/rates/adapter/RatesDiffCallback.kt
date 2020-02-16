package com.ronybrosh.currencyrateapp.presentation.features.rates.adapter

import androidx.recyclerview.widget.DiffUtil
import com.ronybrosh.currencyrateapp.presentation.features.common.model.UIChangePayload
import com.ronybrosh.currencyrateapp.presentation.features.rates.model.UIRate

class RatesDiffCallback(
    private val oldList: List<UIRate>,
    private val newList: List<UIRate>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].currencyCode == newList[newItemPosition].currencyCode
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        if (oldItem.rate != newItem.rate)
            return false

        if (oldItem.convertedAmount != newItem.convertedAmount)
            return false

        return true
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]

        return UIChangePayload(
            oldItem,
            newItem
        )
    }
}