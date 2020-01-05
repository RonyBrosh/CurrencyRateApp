package com.ronybrosh.revolutandroidtest.presentation.util

import android.content.Context
import android.view.inputmethod.InputMethodManager


object KeyboardUtil {
    fun hideKeyboard(context: Context) {
        val imm: InputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(0, 0)
    }
}