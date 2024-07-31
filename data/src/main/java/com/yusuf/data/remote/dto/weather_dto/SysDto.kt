package com.yusuf.data.remote.dto.weather_dto

data class SysDto(
    val country: String,
    val id: Int,
    val sunrise: Int,
    val sunset: Int,
    val type: Int
)