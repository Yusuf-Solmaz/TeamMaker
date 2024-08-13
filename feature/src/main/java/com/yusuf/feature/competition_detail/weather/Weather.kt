package com.yusuf.feature.competition_detail.weather

import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.yusuf.component.LoadingLottie
import com.yusuf.domain.model.weather.CurrentWeatherModel
import com.yusuf.feature.R
import com.yusuf.utils.getLottieAnimationResource

@Composable
fun Weather(
    weatherViewModel: WeatherViewModel = hiltViewModel(),
    location: Location,
    locationName: String,
    weatherName: (CurrentWeatherModel) -> Unit
) {
    val currentWeatherState by weatherViewModel.currentWeatherUIState.collectAsState()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            currentWeatherState.isLoading -> {
                WeatherCard(loading = true)
            }
            currentWeatherState.currentWeather != null -> {
                weatherName(currentWeatherState.currentWeather!!)
                WeatherCard(weatherModel = currentWeatherState.currentWeather!!, locationName = locationName)
            }
            currentWeatherState.error != null -> {
                val errorMessage = currentWeatherState.error
                Text(text = "Error: $errorMessage")
            }
        }
    }

    // Trigger fetching weather data
    LaunchedEffect(true) {
            try {
                Log.d("WeatherComponent", "Fetching weather data for location: ${location.latitude}, ${location.longitude}")
                weatherViewModel.getCurrentWeather(lat = location.latitude, lon = location.longitude)
            } catch (e: Exception) {
                Log.e("WeatherComponent", "Error fetching weather data", e)
            }
    }
}

@Composable
fun WeatherCard(weatherModel: CurrentWeatherModel? =null, locationName: String? = null,loading: Boolean = false) {

    Card(
        modifier = Modifier
            .padding(8.dp)
            .heightIn(min = 120.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        if (loading){
            Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.Center) {
                LoadingLottie(R.raw.image_loading, height = 120.dp)
            }
        }
        else{
            val lottieAnimationResource = weatherModel!!.weatherModel?.firstOrNull()?.getLottieAnimationResource() ?: R.raw.broken_clouds_anim
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            // Static Lottie animation for weather icon
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieAnimationResource))
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
                modifier =  Modifier.size(80.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Weather information
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = locationName!!,
                    style = TextStyle(
                        fontFamily = FontFamily(Font(R.font.splash_title_font)),
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "${weatherModel.mainModel?.temp}°C",
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black,
                    )
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Min: ${weatherModel.mainModel?.tempMin}°C Max: ${weatherModel.mainModel?.tempMax}°C",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = weatherModel.weatherModel?.firstOrNull()?.main.orEmpty(),
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black,
                    )
                )
            }
        }
        }
    }
}