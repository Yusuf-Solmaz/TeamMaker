package com.yusuf.data.remote.service

import com.yusuf.data.remote.dto.CurrentWeatherDto
import com.yusuf.data.util.Constants
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String = Constants.API_KEY,
        @Query("units") units: String = "metric"
    ): CurrentWeatherDto
}