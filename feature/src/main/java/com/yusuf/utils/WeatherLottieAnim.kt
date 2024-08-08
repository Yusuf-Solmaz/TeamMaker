package com.yusuf.utils

import com.yusuf.domain.model.weather.WeatherModel
import com.yusuf.feature.R

fun WeatherModel.getLottieAnimationResource() : Int{
    return when(this.description){
        "clear sky" -> R.raw.clear_sky_anim
        "few clouds" -> R.raw.few_clouds_anim
        "scattered clouds" -> R.raw.few_clouds_anim
        "broken clouds" -> R.raw.broken_clouds_anim
        "shower rain" -> R.raw.shower_rain_anim
        "rain" -> R.raw.rainy_anim
        "thunderstorm" -> R.raw.thunderstorm_anim
        "snow" -> R.raw.snow_anim
        "mist" -> R.raw.mist_anim
        else -> R.raw.few_clouds_anim
    }
}