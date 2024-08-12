package com.yusuf.feature.competition_detail

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.domain.model.competition_detail.CompetitionDetail
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.domain.model.weather.CurrentWeatherModel
import com.yusuf.feature.competition_detail.viewmodel.CompetitionDetailViewModel
import com.yusuf.feature.competition_detail.weather.Weather
import com.yusuf.feature.competition_detail.weather.WeatherCard
import com.yusuf.navigation.NavigationGraph

@SuppressLint("SuspiciousIndentation")@Composable
fun CompetitionDetailScreen(
    navController: NavController,
    competitionDetail: CompetitionDetail? = null,
    savedCompetitionDetail: SavedCompetitionsModel? = null,
    competitionDetailViewModel: CompetitionDetailViewModel = hiltViewModel()
) {

    var sentWeatherModel by remember {
        mutableStateOf(
            CurrentWeatherModel(
                0, "", mainModel = null, null
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier.fillMaxWidth()
            ) {

                val imageUrl = competitionDetail?.imageUrl ?: savedCompetitionDetail?.imageUrl
                if (!imageUrl.isNullOrEmpty()) {
                    Card(
                        modifier = Modifier
                            .size(width = 200.dp, height = 120.dp)
                            .padding(8.dp)
                            .background(Color.White),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(8.dp)
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Competition Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(RoundedCornerShape(16.dp)),
                            alignment = Alignment.Center
                        )
                    }
                }


                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.
                    fillMaxWidth()
                ) {
                    Text(
                        text = competitionDetail?.selectedDate
                            ?: savedCompetitionDetail?.competitionDate!!,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp)) // Add some space between date and time
                    Text(
                        text = competitionDetail?.selectedTime
                            ?: savedCompetitionDetail?.competitionTime!!,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            if (competitionDetail != null) {
                if (competitionDetail.location != null && competitionDetail.locationName != null){
                    Weather(
                        location = competitionDetail.location!!,
                        locationName = competitionDetail.locationName!!
                    ) { weather ->
                        sentWeatherModel = weather
                    }
                }
            } else if (savedCompetitionDetail != null) {
                if (savedCompetitionDetail.weatherModel != null && savedCompetitionDetail.locationName != ""){
                        WeatherCard(
                            weatherModel = savedCompetitionDetail.weatherModel!!,
                            locationName = savedCompetitionDetail.locationName
                        )
                }

            }

            Spacer(modifier = Modifier.height(16.dp))

            TeamListScreen(
                firstTeam = competitionDetail?.firstBalancedTeam
                    ?: savedCompetitionDetail?.firstTeam!!,
                secondTeam = competitionDetail?.secondBalancedTeam
                    ?: savedCompetitionDetail?.secondTeam!!
            )
        }

        if (competitionDetail != null) {
            val savedCompetition = SavedCompetitionsModel(
                firstTeam = competitionDetail.firstBalancedTeam,
                secondTeam = competitionDetail.secondBalancedTeam,
                imageUrl = competitionDetail.imageUrl ?: "",
                competitionTime = competitionDetail.selectedTime,
                competitionDate = competitionDetail.selectedDate,
                locationName = competitionDetail.locationName!!,
                weatherModel = sentWeatherModel,
                competitionName = competitionDetail.competitionName
            )
            AuthButtonComponent(
                value = "Save Competition",
                onClick = {
                    competitionDetailViewModel.saveCompetition(savedCompetition)
                    navController.navigate(NavigationGraph.SAVED_COMPETITIONS.route)
                },
                modifier = Modifier.width(210.dp),
                fillMaxWidth = false,
                heightIn = 36.dp
            )
        }
    }
}





