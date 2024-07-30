package com.examples.weather.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examples.weather.data.Repository
import com.examples.weather.entities.SearchResult
import com.examples.weather.entities.SearchResultList
import com.examples.weather.presentation.states.SearchState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "MyLog"
class SearchViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    var coordinate: SearchResult? = null

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Success)
    val searchState = _searchState.asStateFlow()

    fun getAltitude(locality: String) {
        viewModelScope.launch {
            _searchState.value = SearchState.Loading
            coordinate = repository.getAltitude(locality).searchResultList[0]
            _searchState.value = SearchState.Success
        }
    }


}