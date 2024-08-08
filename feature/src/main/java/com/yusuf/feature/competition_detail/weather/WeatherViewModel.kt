package com.yusuf.feature.competition_detail.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.use_cases.GetCurrentWeatherUseCase
import com.yusuf.domain.util.RootResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getCurrentWeatherUseCase: GetCurrentWeatherUseCase
) : ViewModel() {

    private val _currentWeatherUIState = MutableStateFlow(WeatherUIState())
    val currentWeatherUIState : StateFlow<WeatherUIState> = _currentWeatherUIState

    fun getCurrentWeather(lat:Double,lon:Double){
        _currentWeatherUIState.value = _currentWeatherUIState.value.copy(isLoading = true)

        getCurrentWeatherUseCase(lat,lon).onEach { resource ->
            when(resource){
                is RootResult.Error -> {
                    _currentWeatherUIState.value = _currentWeatherUIState.value.copy(
                        error = resource.message,
                        isLoading = false
                        )
                }
                is RootResult.Loading -> {
                    _currentWeatherUIState.value = _currentWeatherUIState.value.copy(isLoading = true)
                }
                is RootResult.Success -> {
                    _currentWeatherUIState.value = _currentWeatherUIState.value.copy(
                        currentWeather = resource.data,
                        isLoading = false
                    )
                }
            }
        }.launchIn(viewModelScope)
    }
}