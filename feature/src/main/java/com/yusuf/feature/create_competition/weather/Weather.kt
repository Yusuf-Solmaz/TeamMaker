package com.yusuf.feature.create_competition.weather

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
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
import com.yusuf.feature.R
import com.yusuf.feature.create_competition.location.LocationViewModel

@Composable
fun Weather(
    viewModel: WeatherViewModel = hiltViewModel(),
    locationViewModel: LocationViewModel = hiltViewModel()
) {
    val currentWeatherState by viewModel.currentWeatherUIState.collectAsState()
    val locationState by locationViewModel.locationUIState.collectAsState()

    Column(
        modifier = Modifier
            .padding(2.dp),
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