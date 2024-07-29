package com.examples.weather.data.models_dto

import com.examples.weather.entities.DailyResult
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyResultDto(
    @Json(name = "time") override val day: List<String>,
    @Json(name = "temperature_2m_max") override val temperature: List<Double>,
    @Json(name = "weather_code") override val weatherCode: List<Int>,
) : DailyResult
