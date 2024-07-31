package com.yusuf.domain.repository

import com.yusuf.domain.model.weather.CurrentWeatherModel
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    fun getCurrentWeather(lat: Double, lon: Double) : Flow<RootResult<CurrentWeatherModel>>
}