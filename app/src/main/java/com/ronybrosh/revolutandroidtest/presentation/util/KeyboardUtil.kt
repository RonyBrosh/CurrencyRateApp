package com.ronybrosh.revolutandroidtest.presentation.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager


object KeyboardUtil {
    fun hideKeyboard(context: Context, view: View) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}