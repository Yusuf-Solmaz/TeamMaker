package com.yusuf.domain.use_cases

import com.yusuf.domain.repository.WeatherRepository
import javax.inject.Inject

class GetCurrentWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
        operator fun invoke(lat:Double,lon:Double) = weatherRepository.getCurrentWeather(lat,lon)
}