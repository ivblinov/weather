package com.examples.weather.data

import android.util.Log
import com.examples.weather.data.api.GeocodingService
import com.examples.weather.data.api.WeatherService
import com.examples.weather.data.models_dto.CurrentWeatherDto
import com.examples.weather.data.models_dto.DailyDto
import com.examples.weather.data.models_dto.HourlyDto
import com.examples.weather.data.models_dto.SearchResultListDto
import javax.inject.Inject
import javax.inject.Singleton

private const val TAG = "MyLog"

@Singleton
class Repository @Inject constructor(
    private val weatherService: WeatherService,
    private val geocodingService: GeocodingService
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

    suspend fun loadDaily(
        latitude: Double,
        longitude: Double,
    ): DailyDto {
        return weatherService.getDaily(
            latitude = latitude,
            longitude = longitude,
        )
    }

    suspend fun loadHourly(
        latitude: Double,
        longitude: Double,
    ): HourlyDto {
        return weatherService.getHourly(
            latitude = latitude,
            longitude = longitude,
        )
    }

    suspend fun getAltitude(locality: String): SearchResultListDto {
        return geocodingService.getAltitude(locality)
    }
}