package com.yusuf.domain.model.weather

data class CurrentWeatherModel(
    val id: Int = 0,
    val name: String = "",
    val mainModel: MainModel? = null,
    val weatherModel: List<WeatherModel>? = null
)
