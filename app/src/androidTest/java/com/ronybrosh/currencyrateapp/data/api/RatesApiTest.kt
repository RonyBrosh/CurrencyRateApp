package com.ronybrosh.currencyrateapp.data.api

import junit.framework.Assert.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.json.JSONObject
import org.junit.BeforeClass
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RatesApiTest {

    companion object {
        lateinit var ratesApi: RatesApi

        @JvmStatic
        @BeforeClass
        fun beforeClass() {
            val retrofit = Retrofit.Builder().apply {
                addConverterFactory(GsonConverterFactory.create())
                addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                baseUrl("https://revolut.duckdns.org/")
                client(OkHttpClient.Builder().apply {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }.build())
            }.build()

            ratesApi = retrofit.create(RatesApi::class.java)
        }
    }


    /**
     * Call api and verify that rates element is returned
     */
    @Test
    fun getRateList_baseEuro_ratesElementNotNull() {
        // Arrange

        // Act
        val jsonElement = ratesApi.getRateList().blockingGet()
        val rates = jsonElement.asJsonObject.getAsJsonObject(RatesApi.RATES_JSON_OBJECT_KEY)

        // Assert
        assertNotNull(rates)
    }

    /**
     * Convert json element to json object
     */
    @Test
    fun convertJsonElementToJsonObject_apiJsonElement_notNull() {
        // Arrange
        val jsonElement = ratesApi.getRateList().blockingGet()

        // Act
        val jsonObject = JSONObject(jsonElement.toString())

        // Assert
        assertNotNull(jsonObject)
    }
}