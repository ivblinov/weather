package com.examples.weather.di

import com.examples.weather.presentation.fragments.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class
    ]
)
interface AppComponent {

    fun inject(fragment: HomeFragment)
}