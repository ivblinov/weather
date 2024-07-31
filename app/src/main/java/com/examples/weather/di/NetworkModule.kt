package com.examples.weather.di

import com.examples.weather.data.api.WeatherService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

private const val BASE_URL = "https://api.open-meteo.com/"

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofitService(
        provideMoshiConverterFactory: MoshiConverterFactory,
        provideOkHttpClient: OkHttpClient,
    ): WeatherService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(provideOkHttpClient)
            .addConverterFactory(provideMoshiConverterFactory)
            .build()
            .create(WeatherService::class.java)
    }

    @Provides
    fun provideMoshiConverterFactory(): MoshiConverterFactory {
        return MoshiConverterFactory.create()
    }

    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val client = OkHttpClient.Builder().build()
        client.connectTimeoutMillis()
        return client
    }
}