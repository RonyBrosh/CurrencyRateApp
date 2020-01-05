package com.ronybrosh.revolutandroidtest.presentation.features

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ronybrosh.revolutandroidtest.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolBar)
    }
}
