package com.examples.weather.presentation.utils

fun checkValidCoordinate(latitude: Double?, longitude: Double?): Boolean {
    return latitude != null && longitude != null
}