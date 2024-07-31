package com.examples.weather.presentation.viewmodels

import android.app.Application
import android.content.Context
import android.location.Geocoder
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.examples.weather.data.Repository
import com.examples.weather.entities.CurrentResult
import com.examples.weather.presentation.states.HomeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val application: Application,
) : AndroidViewModel(application) {

    var currentWeather: CurrentResult? = null
    var address: String? = null

    private val _currentWeatherState = MutableStateFlow<HomeState>(HomeState.Success)
    val currentWeatherState = _currentWeatherState.asStateFlow()

    private val _geocoderState = MutableStateFlow<HomeState>(HomeState.Success)
    val geocoderState = _geocoderState.asStateFlow()

    fun getWeather(latitude: Double, longitude: Double, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _currentWeatherState.value = HomeState.Loading
            getNameLocality(
                latitude = latitude,
                longitude = longitude,
                context
            )
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

    fun getNameLocality(latitude: Double, longitude: Double, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            _geocoderState.value = HomeState.Loading
            try {
                Geocoder(context, Locale("ru"))
                    .getAddress(latitude, longitude) { addressList: android.location.Address? ->
                        if (addressList != null) {
                            address = addressList.locality
                        }
                    }
            } catch (e: IOException) {
                e.stackTrace
            } catch (e: Exception) {
                e.stackTrace
            } finally {
                _geocoderState.value = HomeState.Success
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun Geocoder.getAddress(
        latitude: Double,
        longitude: Double,
        address: (android.location.Address?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocation(latitude, longitude, 1) { address(it.firstOrNull()) }
            return
        }
        try {
            address(getFromLocation(latitude, longitude, 1)?.firstOrNull())
        } catch(e: Exception) {
            address(null)
        }
    }
}