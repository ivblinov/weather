package com.examples.weather.data

import com.examples.weather.data.api.WeatherService
import com.examples.weather.data.models_dto.CurrentWeatherDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(
    private val weatherService: WeatherService
) {
    suspend fun loadCurrentWeather(
        latitude: Double,
        longitude: Double,
        current: List<String>
    ): CurrentWeatherDto {
        return weatherService.getCurrentWeather(
            latitude = latitude,
            longitude = longitude,
            current = current
        )
    }
}