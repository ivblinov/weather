package com.examples.weather.presentation.states

sealed class HomeState {
    data object Loading : HomeState()
    data object Success : HomeState()
}