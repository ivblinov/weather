package com.examples.weather.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examples.weather.data.Repository
import com.examples.weather.entities.Daily
import com.examples.weather.entities.Hourly
import com.examples.weather.presentation.states.HomeState
import com.examples.weather.presentation.utils.checkValidCoordinate
import com.examples.weather.presentation.utils.getHourFromDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val repository: Repository,
) : ViewModel() {

    private val _detailState = MutableStateFlow<HomeState>(HomeState.Success)
    val detailState = _detailState.asStateFlow()

    private val _hourlyState = MutableStateFlow<HomeState>(HomeState.Success)
    val hourlyState = _hourlyState.asStateFlow()

    var daily: Daily? = null
    var hourly: Hourly? = null
    private var currentTime: String? = null
    var startIndex: Int? = null

    init {
        currentTime = getCurrentTime()
    }

    fun getDaily(
        latitude: Double?,
        longitude: Double?,
    ) {
        if (checkValidCoordinate(latitude, longitude)) {
            viewModelScope.launch {
                _detailState.value = HomeState.Loading
                daily = repository.loadDaily(latitude!!, longitude!!)
                _detailState.value = HomeState.Success
            }
        }
    }

    fun getHourly(
        latitude: Double?,
        longitude: Double?,
    ) {
        if (checkValidCoordinate(latitude, longitude)) {
            viewModelScope.launch(Dispatchers.IO) {
                _hourlyState.value = HomeState.Loading
                hourly = repository.loadHourly(latitude!!, longitude!!)
                formatHourlyList(hourly)
                _hourlyState.value = HomeState.Success
            }
        }
    }

    private fun formatHourlyList(hourly: Hourly?) {
        val dateList = hourly?.hourly?.hour ?: mutableListOf()
        for (i in dateList) {
            val hour = getHourFromDate(i)
            if (hour == currentTime) {
                startIndex = dateList.indexOf(i)
                break
            }
        }
    }

    private fun getCurrentTime(): String {
        val currentDate = Date()
        val timeFormat = SimpleDateFormat("HH", Locale.getDefault())
        val timeText: String = timeFormat.format(currentDate)
        return timeText
    }
}