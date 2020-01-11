package com.ronybrosh.revolutandroidtest.presentation.features.rates.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ronybrosh.revolutandroidtest.R
import com.ronybrosh.revolutandroidtest.domain.model.Rate
import com.ronybrosh.revolutandroidtest.domain.model.Resource
import com.ronybrosh.revolutandroidtest.domain.repositories.RatesRepository
import com.ronybrosh.revolutandroidtest.presentation.features.common.model.UIError
import com.ronybrosh.revolutandroidtest.presentation.features.rates.model.UIRate
import com.ronybrosh.revolutandroidtest.presentation.features.rates.model.UIRateResource
import com.ronybrosh.revolutandroidtest.presentation.util.RateUtil
import com.ronybrosh.revolutandroidtest.presentation.util.UIRateUtil
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import java.util.concurrent.TimeUnit

class RatesViewModel(
    private val ratesRepository: RatesRepository,
    private val uiRateUtil: UIRateUtil
) :
    ViewModel() {
    private val compositeDisposable = CompositeDisposable()
    private val error = MutableLiveData<UIError?>()
    private val loading = MutableLiveData<Boolean>()
    private val result = MutableLiveData<UIRateResource>()

    private var currentAmount = 1F
    private var currentCurrencyRate = 1F

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getError(): LiveData<UIError?> {
        return error
    }

    fun getLoading(): LiveData<Boolean> {
        return loading
    }

    fun getResult(): LiveData<UIRateResource> {
        return result
    }

    fun updateRatesConversion(amountAsString: String, currencyRate: Float) {
        currentCurrencyRate = currencyRate
        currentAmount = RateUtil.convertStringAmountToFloat(amountAsString)
        onUpdateRateList()
    }

    fun moveToTopOfList(fromIndex: Int) {
        synchronized(this) {
            result.value = UIRateResource(
                isScrollToTop = true,
                content = uiRateUtil.moveToTopOfList(fromIndex, result.value?.content)
            )
        }
    }

    fun toggleRefreshRatesInterval(isStart: Boolean) {
        if (isStart)
            startFetchingRatesInterval()
        else
            compositeDisposable.clear()
    }

    private fun startFetchingRatesInterval() {
        val disposable = Observable.interval(1, TimeUnit.SECONDS)
            .startWith(0)
            .flatMap { ratesRepository.getRateList() }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { resource ->
                when (resource) {
                    is Resource.Loading -> loading.value = true
                    is Resource.Success -> {
                        if (loading.value != false)
                            loading.value = false

                        if (error.value != null)
                            error.value = null

                        onFetchedNewRateList(resource.data)
                    }
                    is Resource.Error -> {
                        loading.value = false
                        error.value = UIError(R.string.rates_screen_get_rates_error)
                    }
                }
            }
        compositeDisposable.clear()
        compositeDisposable.add(disposable)
    }

    private fun onUpdateRateList() {
        synchronized(this) {
            val newList = arrayListOf<UIRate>()
            result.value?.content?.forEach {
                val nextUIRate = UIRate(
                    iconResourceId = it.iconResourceId,
                    currencyCode = it.currencyCode,
                    currencyName = it.currencyName,
                    rate = it.rate,
                    convertedAmount = RateUtil.convertRatesToString(
                        amount = currentAmount,
                        fromCurrencyRate = currentCurrencyRate,
                        toCurrencyRate = it.rate
                    )
                )
                newList.add(nextUIRate)
            }
            result.value = UIRateResource(isScrollToTop = false, content = newList)
        }
    }

    private fun onFetchedNewRateList(newRateList: List<Rate>) {
        synchronized(this) {
            result.value = UIRateResource(
                isScrollToTop = false,
                content = uiRateUtil.createNewUpdatedList(
                    amount = currentAmount,
                    currencyRate = currentCurrencyRate,
                    oldList = result.value?.content,
                    newList = newRateList
                )
            )
        }
    }
}