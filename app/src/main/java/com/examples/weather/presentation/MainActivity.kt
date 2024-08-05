package com.examples.weather.presentation

import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.examples.weather.R
import com.examples.weather.presentation.fragments.KEY_FIRST_OPENING
import com.examples.weather.presentation.fragments.PREFERENCE_NAME

class MainActivity : AppCompatActivity() {

    private var prefs: SharedPreferences? = null
    private var editor: Editor? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        prefs = getSharedPreferences(PREFERENCE_NAME, MODE_PRIVATE)
        editor = prefs?.edit()
    }

    override fun onPause() {
        super.onPause()
        editor?.putBoolean(KEY_FIRST_OPENING, false)?.apply()
    }
}