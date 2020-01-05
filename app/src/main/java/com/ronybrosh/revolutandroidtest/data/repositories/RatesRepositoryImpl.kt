package com.ronybrosh.revolutandroidtest.data.repositories

import com.ronybrosh.revolutandroidtest.data.api.ApiUtil
import com.ronybrosh.revolutandroidtest.data.api.RatesApi
import com.ronybrosh.revolutandroidtest.domain.model.Rate
import com.ronybrosh.revolutandroidtest.domain.model.Resource
import com.ronybrosh.revolutandroidtest.domain.repositories.RatesRepository
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import javax.inject.Inject

class RatesRepositoryImpl @Inject constructor(private val ratesApi: RatesApi) : RatesRepository {

    override fun getRateList(): Observable<Resource<List<Rate>>> {
        // 1. Create loading source for displaying loading animation
        val firstSource = Single.just<Resource<List<Rate>>>(Resource.Loading())

        // 2. Create second source for the actual result
        val secondSource =
            ratesApi.getRateList().subscribeOn(Schedulers.io()).map<Resource<List<Rate>>> {
                val jsonObject = JSONObject(it.toString())
                val rateList: List<Rate> = ApiUtil.convertRatesJsonObjectToRateList(jsonObject.getJSONObject(RatesApi.RATES_JSON_OBJECT_KEY))
                Resource.Success(rateList)
            }.onErrorReturn {
                Resource.Error(it)
            }

        // 3. Concat the sources to keep the order
        return Observable.concat(firstSource.toObservable(), secondSource.toObservable())
    }
}