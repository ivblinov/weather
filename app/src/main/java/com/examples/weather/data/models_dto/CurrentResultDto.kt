package com.examples.weather.data.models_dto

import com.examples.weather.entities.CurrentResult
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentResultDto(
    @Json(name = "time") override val time: String,
    @Json(name = "temperature_2m") override val temperature: Double,
    @Json(name = "wind_speed_10m") override val windSpeed: Double,
    @Json(name = "weather_code") override val weatherCode: Int,
    @Json(name = "relative_humidity_2m") override val relativeHumidity: Int
) : CurrentResult