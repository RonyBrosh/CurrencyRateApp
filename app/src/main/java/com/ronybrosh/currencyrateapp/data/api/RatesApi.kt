package com.ronybrosh.currencyrateapp.data.api

import com.google.gson.JsonElement
import io.reactivex.Single
import retrofit2.http.GET

interface RatesApi {
    companion object{
        const val RATES_JSON_OBJECT_KEY = "rates"
    }

    @GET("latest?base=EUR")
    fun getRateList(): Single<JsonElement>
}