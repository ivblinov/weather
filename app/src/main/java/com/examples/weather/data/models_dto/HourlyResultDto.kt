package com.examples.weather.data.models_dto

import com.examples.weather.entities.HourlyResult
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyResultDto(
    @Json(name = "time") override val hour: MutableList<String>,
    @Json(name = "temperature_2m") override val temperature: MutableList<Double>,
    @Json(name = "weather_code") override val weatherCode: MutableList<Int>,
) : HourlyResult