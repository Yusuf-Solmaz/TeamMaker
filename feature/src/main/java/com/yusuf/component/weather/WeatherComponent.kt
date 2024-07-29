package com.yusuf.component.weather

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
import com.yusuf.domain.util.RootResult

@Composable
fun WeatherComponent(
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val currentWeatherState by viewModel.currentWeatherState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (currentWeatherState) {
            is RootResult.Loading -> {
                Log.d("WeatherComponent", "Loading weather data...")
                CircularProgressIndicator()
            }
            is RootResult.Success -> {
                val weather = (currentWeatherState as RootResult.Success).data
                Log.d("WeatherComponent", "Weather data loaded: ${weather?.mainModel?.temp}°C")
                Text(text = "Current Weather: ${weather?.mainModel?.temp}°C")
                Text(text = "Min Temp: ${weather?.mainModel?.tempMin}°C")
                Text(text = "Max Temp: ${weather?.mainModel?.tempMax}°C")
                Text(text = "Weather: ${weather?.weatherModel?.firstOrNull()?.main}")

            }
            is RootResult.Error -> {
                val errorMessage = (currentWeatherState as RootResult.Error).message
                Log.e("WeatherComponent", "Error loading weather data: $errorMessage")
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