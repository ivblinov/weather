package com.examples.weather.entities

interface HourlyResult {
    val hour: MutableList<String>
    val temperature: MutableList<Double>
    val weatherCode: MutableList<Int>
}