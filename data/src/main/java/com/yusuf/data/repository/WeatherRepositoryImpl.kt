package com.yusuf.data.repository

import com.yusuf.data.mapper.toCurrentWeatherModel
import com.yusuf.data.remote.service.WeatherApiService
import com.yusuf.domain.model.weather.CurrentWeatherModel
import com.yusuf.domain.repository.WeatherRepository
import com.yusuf.domain.util.RootResult
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ViewModelScoped
class WeatherRepositoryImpl @Inject constructor(
    private val weatherApiService: WeatherApiService
) : WeatherRepository {

    override fun getCurrentWeather(
        lat: Double,
        lon: Double
    ): Flow<RootResult<CurrentWeatherModel>> = flow {
        emit(RootResult.Loading)
        val currentWeatherResponseDto = weatherApiService.getCurrentWeather(lat, lon)
        val currentWeatherModel = currentWeatherResponseDto.toCurrentWeatherModel()
        emit(RootResult.Success(currentWeatherModel))
    }.flowOn(Dispatchers.IO)
        .catch {
            emit(RootResult.Error(it.message.toString()))
        }
}