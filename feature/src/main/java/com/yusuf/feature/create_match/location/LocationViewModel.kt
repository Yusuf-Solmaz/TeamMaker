package com.yusuf.feature.create_match.location

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.use_cases.location.GetLocationNameUseCase
import com.yusuf.domain.use_cases.location.GetLocationUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    application: Application,
    private val locationUseCase: GetLocationUseCase,
    private val locationNameUseCase: GetLocationNameUseCase
) : AndroidViewModel(application) {

    private val _locationUIState = MutableStateFlow(LocationUIState(isLoading = true))
    val locationUIState: StateFlow<LocationUIState> = _locationUIState

    fun fetchLocation() {
        _locationUIState.value = _locationUIState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val location = locationUseCase()
                if (location != null) {
                    val locationName = locationNameUseCase(location.latitude, location.longitude)
                    _locationUIState.value = _locationUIState.value.copy(
                        location = location,
                        locationName = locationName,
                        isLoading = false
                    )
                } else {
                    _locationUIState.value = _locationUIState.value.copy(
                        error = "Location not found",
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _locationUIState.value = _locationUIState.value.copy(
                    error = e.message ?: "Unknown error",
                    isLoading = false
                )
            }
        }
    }

    fun checkPermissions(): Boolean {
        val context = getApplication<Application>().applicationContext
        return ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}