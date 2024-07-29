package com.examples.weather.di

import com.examples.weather.presentation.fragments.DetailFragment
import com.examples.weather.presentation.fragments.HomeFragment
import com.examples.weather.presentation.fragments.SearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        NetworkModule::class,
        SearchNetworkModule::class,
    ]
)
interface AppComponent {

    fun inject(fragment: HomeFragment)
    fun inject(fragment: SearchFragment)
    fun inject(fragment: DetailFragment)
}