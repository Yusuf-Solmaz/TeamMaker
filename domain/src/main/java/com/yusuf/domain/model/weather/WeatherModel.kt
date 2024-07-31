package com.yusuf.domain.model.weather

data class WeatherModel(
    val id: Int,
    val main: String,
    val description: String,
    val icon: String
)
