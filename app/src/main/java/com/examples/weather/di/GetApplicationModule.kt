package com.examples.weather.di

import android.app.Application
import dagger.Module
import dagger.Provides

@Module
class GetApplicationModule {

    @Provides
    fun provideApplication(): Application {
        return Application()
    }
}