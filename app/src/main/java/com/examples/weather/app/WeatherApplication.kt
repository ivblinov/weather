package com.examples.weather.app

import android.app.Application
import com.examples.weather.di.AppComponent
import com.examples.weather.di.DaggerAppComponent

class WeatherApplication : Application() {

    val appComponent: AppComponent = DaggerAppComponent.create()
}