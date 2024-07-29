package com.yusuf.data.remote.dto

data class CurrentWeatherDto(
    val base: String,
    val clouds: CloudsDto,
    val cod: Int,
    val coord: CoordDto,
    val dt: Int,
    val id: Int,
    val main: MainDto,
    val name: String,
    val sys: SysDto,
    val timezone: Int,
    val visibility: Int,
    val weather: List<WeatherDto>,
    val wind: WindDto
)