package com.examples.weather.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.examples.weather.data.Repository
import com.examples.weather.entities.SearchResult
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
        viewModelScope.launch(Dispatchers.IO) {
            _searchState.value = SearchState.Loading
            Log.d(TAG, "getAltitude: loading")
            Log.d(TAG, "coordinate = $coordinate")
//            coordinate = repository.getAltitude(locality).searchResultList[0]
            try {
                val response = repository.getAltitude(locality)
                Log.d(TAG, "code = ${response.code()}")
                coordinate = response.body()?.searchResultList?.get(0)
                _searchState.value = SearchState.Success
            } catch (e: com.squareup.moshi.JsonDataException) {
                e.stackTrace
                Log.d(TAG, "e = ${e.message}")
                _searchState.value = SearchState.Error
            }
        }
    }


}