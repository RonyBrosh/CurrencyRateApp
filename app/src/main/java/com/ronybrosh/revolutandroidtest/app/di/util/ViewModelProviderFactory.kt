package com.ronybrosh.revolutandroidtest.app.di.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

@Suppress("UNCHECKED_CAST")
class ViewModelProviderFactory @Inject constructor(private val creators: Map<Class<out ViewModel>, Provider<ViewModel>>) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        var creator: Provider<ViewModel>? = creators[modelClass]
        if (creator == null) {
            // if the view model has not been created
            // loop through the allowable keys (aka allowed classes with the @ViewModelKey)
            creators.forEach {
                // if it's allowed, set the Provider<ViewModel>
                if (modelClass.isAssignableFrom(it.key)) {
                    creator = it.value
                    return@forEach
                }
            }
        }

        // if this is not one of the allowed keys, throw exception
        if (creator == null) {
            throw IllegalArgumentException("Unknown model class $modelClass")
        }

        // return the Provider
        try {
            return creator?.get() as T
        } catch (ex: Exception) {
            throw RuntimeException(ex)
        }
    }
}