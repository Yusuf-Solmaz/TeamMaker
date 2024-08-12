package com.yusuf.utils

import com.yusuf.domain.model.weather.WeatherModel
import com.yusuf.feature.R

fun WeatherModel.getLottieAnimationResource() : Int{
    return when(this.main){
        "Clear" -> R.raw.clear_sky_anim
        "Clouds" -> R.raw.few_clouds_anim
        "Rain" -> R.raw.shower_rain_anim
        "Drizzle" -> R.raw.rainy_anim
        "Thunderstorm" -> R.raw.thunderstorm_anim
        "Snow" -> R.raw.snow_anim
        "Atmosphere" -> R.raw.mist_anim
        else -> R.raw.few_clouds_anim
    }
}