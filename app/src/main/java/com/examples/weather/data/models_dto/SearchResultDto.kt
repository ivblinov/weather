package com.examples.weather.data.models_dto

import com.examples.weather.entities.SearchResult
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResultDto(
    @Json(name = "latitude") override val latitude: Double,
    @Json(name = "longitude") override val longitude: Double,
) : SearchResult