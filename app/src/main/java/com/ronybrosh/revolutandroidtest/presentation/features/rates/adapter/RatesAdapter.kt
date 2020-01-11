package com.ronybrosh.revolutandroidtest.presentation.features.rates.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.ronybrosh.revolutandroidtest.R
import com.ronybrosh.revolutandroidtest.presentation.features.common.model.UIChangePayload
import com.ronybrosh.revolutandroidtest.presentation.features.common.model.convertToSingleUIChangePayload
import com.ronybrosh.revolutandroidtest.presentation.features.rates.model.UIRate
import kotlinx.android.synthetic.main.viewholder_rate_item.view.*

class RatesAdapter(private val listener: RateAdapterListener) :
    RecyclerView.Adapter<RatesAdapter.RateViewHolder>() {

    private val content = mutableListOf<UIRate>()

    interface RateAdapterListener {
        fun updateRatesConversion(amountAsString: String, currencyRate: Float)
        fun updateTopItemIndex(oldIndex: Int)
        fun onNoFocusedAmountField()
    }

    inner class RateViewHolder(view: View) : RecyclerView.ViewHolder(view), TextWatcher,
        View.OnFocusChangeListener, View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
            itemView.amountInput.onFocusChangeListener = this
            itemView.amountInput.addTextChangedListener(this)
        }

        fun bind(uiRate: UIRate) {
            itemView.icon.setImageResource(uiRate.iconResourceId)
            itemView.currencyCode.text = uiRate.currencyCode
            itemView.currencyName.text = uiRate.currencyName
            itemView.amountInput.setText(uiRate.convertedAmount)
        }

        fun bind(oldItem: UIRate, newItem: UIRate) {
            // Don't update the top most item amount where the user may be typing
            if (adapterPosition == 0)
                return

            if (oldItem.convertedAmount != newItem.convertedAmount)
                itemView.amountInput.setText(newItem.convertedAmount)
        }

        override fun afterTextChanged(s: Editable?) {
            // Each refresh rate interval that is automatically invoked by the view model will trigger 'afterTextChanged'
            // In order to avoid the redundant calls we need to make sure that:
            // 1. The adapter position is valid
            // 2. The adapter position is 0.
            // Condition 2 will avoid triggering while the input is just gained focus and animated to the top
            // 3. That the change triggered by the user, which means a "Focused" input
            if (adapterPosition == RecyclerView.NO_POSITION || adapterPosition != 0 || !itemView.amountInput.hasFocus())
                return

            val uiRate: UIRate = content[adapterPosition]
            listener.updateRatesConversion(
                amountAsString = s.toString(),
                currencyRate = uiRate.rate
            )
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            // If the focused input is not the top most, update it to top index
            if (hasFocus.and(adapterPosition > 0)) {
                listener.updateTopItemIndex(adapterPosition)
                return
            }

            // If amount edit text lost focus, maybe we don't have any focused amount edit text.
            // In this case we should hide the keyboard.
            // This will be handled by the context (activity/ fragment)
            // because we need the parent (recycler view) to verify no other child (edit text) have focus.
            if (!hasFocus)
                listener.onNoFocusedAmountField()
        }

        override fun onClick(v: View?) {
            if (adapterPosition == RecyclerView.NO_POSITION)
                return

            itemView.amountInput.requestFocus()

            if (adapterPosition > 0)
                listener.updateTopItemIndex(adapterPosition)
        }
    }

    override fun getItemCount(): Int {
        return content.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RateViewHolder {
        return RateViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.viewholder_rate_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RateViewHolder, position: Int) {
        holder.bind(content[position])
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(
        holder: RateViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else {
            val uiChangePayload: UIChangePayload<UIRate> =
                convertToSingleUIChangePayload(payloads as List<UIChangePayload<UIRate>>)
            holder.bind(uiChangePayload.oldData, uiChangePayload.newData)
        }
    }

    fun setContent(newContent: List<UIRate>) {
        val result = DiffUtil.calculateDiff(RatesDiffCallback(content, newContent))
        result.dispatchUpdatesTo(this)
        content.clear()
        content.addAll(newContent)
    }

}