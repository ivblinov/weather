package com.examples.weather.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import javax.inject.Inject

private const val TAG = "MyLog"
class HomeViewModel @Inject constructor() : ViewModel() {

    fun getWeather(latitude: Double, longitude: Double) {
        Log.d(TAG, "longitude = $longitude")
        Log.d(TAG, "latitude = $latitude")
    }
}