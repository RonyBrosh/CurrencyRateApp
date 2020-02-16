package com.ronybrosh.currencyrateapp.data.api

import com.ronybrosh.currencyrateapp.domain.model.Rate
import org.json.JSONObject

object ApiUtil {
    fun convertRatesJsonObjectToRateList(json: JSONObject): List<Rate> {
        val result = arrayListOf<Rate>()

        // Add base rate. At the moment it is hard coded, in the future it can be passed as parameter.
        result.add(Rate(currency = "EUR", rate = 1F))

        json.keys().forEach {
            val currency: String = it
            val rate: Float = (json.get(it) as Double).toFloat()
            result.add(Rate(currency = currency, rate = rate))
        }
        return result.toList()
    }
}