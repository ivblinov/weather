package com.examples.weather.data.api

import com.examples.weather.data.models_dto.CurrentWeatherDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherService {

    @GET("/v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: List<String>,
        @Query("timezone") timezone: String = "auto",
    ): CurrentWeatherDto
}