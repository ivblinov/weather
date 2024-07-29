package com.examples.weather.di

import com.examples.weather.data.api.GeocodingService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://geocoding-api.open-meteo.com/"

@Module
class SearchNetworkModule {

    @Singleton
    @Provides
    fun provideSearchRetrofitService(
        moshiConverterFactory: MoshiConverterFactory
    ): GeocodingService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()
            .create(GeocodingService::class.java)
    }
}