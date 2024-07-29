package com.examples.weather.data.api

import com.examples.weather.data.models_dto.CurrentWeatherDto
import com.examples.weather.data.models_dto.DailyDto
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

    @GET("/v1/forecast")
    suspend fun getDaily(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("timezone") timezone: String = "auto",
        @Query("daily") daily: List<String> = listOf("temperature_2m_max", "weather_code"),
        @Query("forecast_days") forecastDays: Int = 11
    ): DailyDto
}