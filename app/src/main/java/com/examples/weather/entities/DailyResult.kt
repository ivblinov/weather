package com.examples.weather.entities

interface DailyResult {
    val day: MutableList<String>
    val temperature: MutableList<Double>
    val weatherCode: MutableList<Int>
}