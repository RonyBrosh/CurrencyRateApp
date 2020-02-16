package com.ronybrosh.currencyrateapp.presentation.features.rates.view

import android.content.Context
import android.net.wifi.WifiManager
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import com.ronybrosh.currencyrateapp.R
import com.ronybrosh.currencyrateapp.presentation.features.MainActivity
import org.hamcrest.Matcher
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class RatesListFragmentTest {

    @Rule
    @JvmField
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    private val wifiManager =
        InstrumentationRegistry.getInstrumentation().targetContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

    @Before
    fun before() {
        println(">>PLEASE NOTE<< all 'RatesListFragmentTest' tests must run independently to pass!")
    }

    /**
     * Verify that loading animation is visible when app start
     */
    @Test
    fun loading_activityStarted_isVisible() {
        // Arrange

        // Act

        // Assert
        onView(withId(R.id.progressBar)).check(matches((isDisplayed())))
    }

    /**
     * Verify that the adapter view is not empty
     */
    @Test
    fun recyclerViewAdapter_validResult_isNotEmpty() {
        // Arrange

        // Act
        // Wait for the result to be available
        Thread.sleep(1000)

        // Assert
        onView(withId(R.id.recyclerView)).check(matches(hasMinimumChildCount(1)))
    }

    /**
     * Verify that clicking and item from the list moves to the top of the list
     */
    @Test
    fun clickingItem_validResult_itemMoveToTopOfTheList() {
        // Arrange
        val clickIndex = 2
        var clickedItemCurrency: String? = null

        // Act
        // Wait for the result to be available
        Thread.sleep(1000)
        onView(withId(R.id.recyclerView)).perform(object : ViewAction {
            override fun getDescription(): String {
                return "Clicking an item"
            }

            override fun getConstraints(): Matcher<View> {
                return click().constraints
            }

            override fun perform(uiController: UiController?, view: View) {
                // Arrange
                val itemView = (view as ViewGroup).getChildAt(clickIndex)
                clickedItemCurrency =
                    itemView.findViewById<TextView>(R.id.currencyCode).text.toString()

                // Act
                itemView.callOnClick()
            }
        })

        // Wait for the move animation to end
        Thread.sleep(2000)
        onView(withId(R.id.recyclerView)).perform(object : ViewAction {
            override fun getDescription(): String {
                return "Asserting item text"
            }

            override fun getConstraints(): Matcher<View> {
                return click().constraints
            }

            override fun perform(uiController: UiController?, view: View) {
                // Arrange
                val itemView = (view as ViewGroup).getChildAt(0)
                val itemCurrencyCode =
                    itemView.findViewById<TextView>(R.id.currencyCode).text.toString()

                // Assert
                assertEquals(clickedItemCurrency, itemCurrencyCode)
            }
        })

    }

    /**
     * Verify that the error is shown
     */
    @Test
    fun error_noInternetConnection_isShown() {
        // Arrange
        wifiManager.isWifiEnabled = false

        // Act
        // Wait for the wifi to close
        Thread.sleep(1000)

        // Assert
        onView(withId(com.google.android.material.R.id.snackbar_text)).check(matches((isDisplayed())))
        wifiManager.isWifiEnabled = true
    }
}