package com.examples.weather.data.models_dto

import com.examples.weather.entities.Hourly
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class HourlyDto(
    @Json(name = "hourly") override val hourly: HourlyResultDto
) : Hourly