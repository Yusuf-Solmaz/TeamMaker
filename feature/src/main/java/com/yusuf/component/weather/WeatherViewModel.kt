package com.yusuf.component.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.CurrentWeatherModel
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

    private val _currentWeatherState = MutableStateFlow<RootResult<CurrentWeatherModel>>(RootResult.Loading)
    val currentWeatherState : StateFlow<RootResult<CurrentWeatherModel>> = _currentWeatherState

    fun getCurrentWeather(lat:Double,lon:Double){
        getCurrentWeatherUseCase(lat,lon).onEach { resource ->
            when(resource){
                is RootResult.Error -> {
                    _currentWeatherState.value = RootResult.Error(resource.message)
                }
                is RootResult.Loading -> {
                    _currentWeatherState.value = RootResult.Loading
                }
                is RootResult.Success -> {
                    _currentWeatherState.value = RootResult.Success(resource.data)
                }
            }
        }.launchIn(viewModelScope)
    }
}