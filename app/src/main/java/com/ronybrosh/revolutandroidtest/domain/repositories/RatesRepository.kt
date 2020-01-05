package com.ronybrosh.revolutandroidtest.domain.repositories

import com.ronybrosh.revolutandroidtest.domain.model.Rate
import com.ronybrosh.revolutandroidtest.domain.model.Resource
import io.reactivex.Observable

interface RatesRepository {
    fun getRateList(): Observable<Resource<List<Rate>>>
}