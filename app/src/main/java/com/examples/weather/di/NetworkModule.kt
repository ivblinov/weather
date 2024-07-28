package com.examples.weather.di

import com.examples.weather.data.api.WeatherService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.open-meteo.com/"

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitService(
        moshiConverterFactory: MoshiConverterFactory
    ): WeatherService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()
            .create(WeatherService::class.java)
    }

    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }
}