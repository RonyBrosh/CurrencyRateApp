package com.ronybrosh.currencyrateapp.data.api

import com.ronybrosh.currencyrateapp.domain.model.Rate
import org.json.JSONObject
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class ApiUtilTest {

    /**
     * Verify that rates json object is converted to list of rate
     */
    @Test
    fun convertRatesJsonObjectToRateList_mockedJsonObject_listOfRateObject() {
        // Arrange
        val jsonObject = JSONObject(javaClass.getResource("/get_rates.json").readText())

        // Act
        val rateList: List<Rate> =
            ApiUtil.convertRatesJsonObjectToRateList(jsonObject.getJSONObject(RatesApi.RATES_JSON_OBJECT_KEY))

        // Assert
        assertTrue(rateList.isNotEmpty())
    }
}