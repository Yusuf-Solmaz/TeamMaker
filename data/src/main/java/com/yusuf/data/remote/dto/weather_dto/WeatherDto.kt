package com.yusuf.data.remote.dto.weather_dto

data class WeatherDto(
    val description: String,
    val icon: String,
    val id: Int,
    val main: String
)