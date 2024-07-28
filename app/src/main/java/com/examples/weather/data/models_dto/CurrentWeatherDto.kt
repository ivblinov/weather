package com.examples.weather.data.models_dto

import com.examples.weather.entities.CurrentWeather
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrentWeatherDto(
    @Json(name = "current") override val current: CurrentResultDto
) : CurrentWeather
