package com.yusuf.feature.create_match.weather

import com.yusuf.domain.model.CurrentWeatherModel

data class WeatherUIState(
    val isLoading: Boolean = false,
    val currentWeather: CurrentWeatherModel? = null,
    val error: String? = null
)
