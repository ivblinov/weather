package com.examples.weather.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examples.weather.data.Repository
import com.examples.weather.entities.Daily
import com.examples.weather.presentation.states.HomeState
import com.examples.weather.presentation.utils.checkValidCoordinate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyLog"
class DetailViewModel @Inject constructor (
    private val repository: Repository
) : ViewModel() {

    private val _detailState = MutableStateFlow<HomeState>(HomeState.Success)
    val detailState = _detailState.asStateFlow()

    var daily: Daily? = null
    var todayMonth: String? = null

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
}