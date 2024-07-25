package com.examples.weather.di

import com.examples.weather.presentation.fragments.HomeFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component()
interface AppComponent {

    fun inject(fragment: HomeFragment)
}