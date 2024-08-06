package com.examples.weather.data.models_dto

import com.examples.weather.entities.SearchResultList
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchResultListDto(
    @Json(name = "results") override val searchResultList: List<SearchResultDto>,
) : SearchResultList
