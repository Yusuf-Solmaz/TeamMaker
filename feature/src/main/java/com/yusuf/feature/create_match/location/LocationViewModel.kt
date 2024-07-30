package com.yusuf.feature.create_match.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.use_cases.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val locationUseCase: GetLocationUseCase
) : ViewModel() {

    private val _locationUIState = MutableStateFlow(LocationUIState(isLoading = true))
    val locationUIState : StateFlow<LocationUIState> = _locationUIState

    fun fetchLocation(){
        _locationUIState.value = _locationUIState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val location = locationUseCase()
                _locationUIState.value = _locationUIState.value.copy(
                    location = location,
                    isLoading = false
                )
            } catch (e: Exception) {
                _locationUIState.value = _locationUIState.value.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }
}