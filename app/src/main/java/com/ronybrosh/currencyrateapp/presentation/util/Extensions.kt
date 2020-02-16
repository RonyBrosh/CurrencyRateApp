package com.ronybrosh.currencyrateapp.presentation.util

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> LifecycleOwner.observe(liveData: LiveData<T>, crossinline lambda: (T) -> Unit) {
    liveData.observe(this, Observer {
        lambda(it)
    })
}