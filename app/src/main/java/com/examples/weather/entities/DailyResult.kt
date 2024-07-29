package com.examples.weather.entities

interface DailyResult {
    val day: List<String>
    val temperature: List<Double>
    val weatherCode: List<Int>
}