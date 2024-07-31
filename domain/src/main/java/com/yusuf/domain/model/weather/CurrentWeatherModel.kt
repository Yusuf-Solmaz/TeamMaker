package com.yusuf.domain.model.weather

data class CurrentWeatherModel(
    val id: Int,
    val name: String,
    val mainModel: MainModel,
    val weatherModel: List<WeatherModel>
)
