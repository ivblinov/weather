package com.examples.weather.presentation.states

sealed class SearchState {
    data object Loading : SearchState()
    data object Success : SearchState()
    data object Error : SearchState()
}