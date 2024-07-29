package com.examples.weather.data.api

import com.examples.weather.data.models_dto.SearchResultListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {

    @GET("/v1/search")
    suspend fun getAltitude(
        @Query("name") name: String,
        @Query("count") count: Int = 1,
        @Query("language") language: String = "ru",
    ): SearchResultListDto
}