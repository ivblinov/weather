package com.examples.weather.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examples.weather.data.Repository
import com.examples.weather.entities.CurrentResult
import com.examples.weather.presentation.states.HomeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyLog"

class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var currentWeather: CurrentResult? = null

    private val _currentWeatherState = MutableStateFlow<HomeState>(HomeState.Success)
    val currentWeatherState = _currentWeatherState.asStateFlow()

    fun getWeather(latitude: Double, longitude: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentWeatherState.value = HomeState.Loading
            currentWeather = repository.loadCurrentWeather(
                latitude = latitude,
                longitude = longitude,
                current = listOf(
                    "temperature_2m",
                    "wind_speed_10m",
                    "weather_code",
                    "relative_humidity_2m",
                )
            ).current
            _currentWeatherState.value = HomeState.Success
        }
    }
}