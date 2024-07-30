package com.yusuf.data.mapper

import com.yusuf.data.remote.dto.CurrentWeatherDto
import com.yusuf.data.remote.dto.MainDto
import com.yusuf.data.remote.dto.WeatherDto
import com.yusuf.domain.model.CurrentWeatherModel
import com.yusuf.domain.model.MainModel
import com.yusuf.domain.model.WeatherModel

// it is an extension function for CurrentWeatherDto class
// it converts CurrentWeatherDto to CurrentWeatherModel object to use in domain layer
fun CurrentWeatherDto.toCurrentWeatherModel(): CurrentWeatherModel{
    return CurrentWeatherModel(
        id = id,
        name = name,
        mainModel = main.toMainModel(),
        weatherModel = weather.map { it.toWeatherModel() }
    )
}

// if the API response will change, it is enough to change the mappers in the data layer and the domain layer will not be affected.
fun MainDto.toMainModel(): MainModel {
    return MainModel(
        temp = temp,
        tempMax = temp_max,
        tempMin = temp_min
    )
}

fun WeatherDto.toWeatherModel(): WeatherModel {
    return WeatherModel(
        id = id,
        main = main,
        description = description,
        icon = icon
    )
}