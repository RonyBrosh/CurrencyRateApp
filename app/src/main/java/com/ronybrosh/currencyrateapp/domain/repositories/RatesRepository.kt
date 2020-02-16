package com.ronybrosh.currencyrateapp.domain.repositories

import com.ronybrosh.currencyrateapp.domain.model.Rate
import com.ronybrosh.currencyrateapp.domain.model.Resource
import io.reactivex.Observable

interface RatesRepository {
    fun getRateList(): Observable<Resource<List<Rate>>>
}