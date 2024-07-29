package com.yusuf.feature.create_match.weather

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusuf.component.LoadingLottie
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.R

@Composable
fun Weather(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val currentWeatherState by viewModel.currentWeatherUIState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            currentWeatherState.isLoading -> {
                LoadingLottie(R.raw.loading_anim)
            }
            currentWeatherState.currentWeather != null -> {
                val weather = currentWeatherState.currentWeather
                Text(text = "Current Weather: ${weather?.mainModel?.temp}°C")
                Text(text = "Min Temp: ${weather?.mainModel?.tempMin}°C")
                Text(text = "Max Temp: ${weather?.mainModel?.tempMax}°C")
                Text(text = "Weather: ${weather?.weatherModel?.firstOrNull()?.main}")
            }
            currentWeatherState.error != null -> {
                val errorMessage = currentWeatherState.error
                Text(text = "Error: $errorMessage")
            }
        }
    }

    // Trigger fetching weather data
    LaunchedEffect(Unit) {
        try {
            Log.d("WeatherComponent", "Fetching weather data...")
            viewModel.getCurrentWeather(lat = 36.92143205499421, lon = 30.803234126259916)
        } catch (e: Exception) {
            Log.e("WeatherComponent", "Error fetching weather data", e)
        }
    }
}