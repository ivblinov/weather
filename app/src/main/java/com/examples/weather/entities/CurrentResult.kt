package com.examples.weather.entities

interface CurrentResult {
    val time: String
    val temperature: Double
    val windSpeed: Double
    val weatherCode: Int
    val relativeHumidity: Int
}