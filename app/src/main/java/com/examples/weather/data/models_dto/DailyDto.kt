package com.examples.weather.data.models_dto

import com.examples.weather.entities.Daily
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DailyDto(
    @Json(name = "daily") override val daily: DailyResultDto,
) : Daily
