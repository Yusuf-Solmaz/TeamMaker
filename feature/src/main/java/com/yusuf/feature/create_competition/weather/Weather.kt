package com.yusuf.feature.create_competition.weather

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.component.LoadingLottie
import com.yusuf.domain.model.weather.CurrentWeatherModel
import com.yusuf.feature.R
import com.yusuf.feature.create_competition.location.LocationViewModel
import com.yusuf.utils.getLottieAnimationResource

@Composable
fun Weather(
    viewModel: WeatherViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val currentWeatherState by viewModel.currentWeatherUIState.collectAsState()
    val locationState by locationViewModel.locationUIState.collectAsState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            currentWeatherState.isLoading -> {
                LoadingLottie(R.raw.loading_anim)
            }
            currentWeatherState.currentWeather != null -> {
//                val weather = currentWeatherState.currentWeather
//                Text(text = "Current Weather: ${weather?.mainModel?.temp}°C")
//                Text(text = "Min Temp: ${weather?.mainModel?.tempMin}°C")
//                Text(text = "Max Temp: ${weather?.mainModel?.tempMax}°C")
//                Text(text = "Weather: ${weather?.weatherModel?.firstOrNull()?.main}")
                WeatherCard(weatherModel = currentWeatherState.currentWeather!!)
            }
            currentWeatherState.error != null -> {
                val errorMessage = currentWeatherState.error
                Text(text = "Error: $errorMessage")
            }
        }
    }

    // Trigger fetching weather data
    LaunchedEffect(locationState.location) {
        locationState.location?.let { location ->
            try {
                Log.d("WeatherComponent", "Fetching weather data for location: ${location.latitude}, ${location.longitude}")
                viewModel.getCurrentWeather(lat = location.latitude, lon = location.longitude)
            } catch (e: Exception) {
                Log.e("WeatherComponent", "Error fetching weather data", e)
            }
        }
    }
}

@Composable
fun WeatherCard(weatherModel: CurrentWeatherModel) {
    val lottieAnimationResource = weatherModel.weatherModel.firstOrNull()?.getLottieAnimationResource() ?: R.raw.broken_clouds_anim

    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Static Lottie animation for weather icon
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieAnimationResource))
            val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

            LottieAnimation(
                composition = composition,
                progress = progress,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Weather information
            Text(text = "Current Temperature: ${weatherModel.mainModel.temp}°C", fontSize = 20.sp, color = Color.Black)
            Text(text = "Min Temperature: ${weatherModel.mainModel.tempMin}°C", fontSize = 16.sp, color = Color.Black)
            Text(text = "Max Temperature: ${weatherModel.mainModel.tempMax}°C", fontSize = 16.sp, color = Color.Black)
            Text(text = "Weather: ${weatherModel.weatherModel.firstOrNull()?.main}", fontSize = 16.sp, color = Color.Black)
        }
    }
}