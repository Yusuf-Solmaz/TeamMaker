package com.yusuf.feature.competition_detail

import android.util.Log
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.yusuf.domain.model.competition_detail.CompetitionDetail
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.domain.model.weather.CurrentWeatherModel
import com.yusuf.domain.model.weather.MainModel
import com.yusuf.domain.model.weather.WeatherModel
import com.yusuf.feature.R
import com.yusuf.feature.competition_detail.viewmodel.CompetitionDetailViewModel
import com.yusuf.feature.competition_detail.weather.Weather
import com.yusuf.feature.competition_detail.weather.WeatherCard
import com.yusuf.navigation.NavigationGraph

@Composable
fun CompetitionDetailScreen(
    navController: NavController,
    competitionDetail: CompetitionDetail? = null,
    savedCompetitionDetail: SavedCompetitionsModel? = null,
    competitionDetailViewModel: CompetitionDetailViewModel = hiltViewModel()
) {

    val saveCompetitionState by competitionDetailViewModel.competitionDetailState.collectAsState()
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
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {

                Text(
                    text = competitionDetail?.selectedTime
                        ?: savedCompetitionDetail?.competitionTime!!,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = competitionDetail?.selectedDate
                        ?: savedCompetitionDetail?.competitionDate!!,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                AsyncImage(
                    model = competitionDetail?.imageUrl
                        ?: savedCompetitionDetail?.imageUrl,
                    contentDescription = "Competition Image",
                    modifier = Modifier.size(100.dp)
                )
            }
            Spacer(modifier = Modifier.height(2.dp))

            if (competitionDetail != null) {
                Weather(
                    location = competitionDetail.location!!,
                    locationName = competitionDetail.locationName!!
                ) { weather ->
                    sentWeatherModel = weather
                }
            } else if (savedCompetitionDetail != null) {
                savedCompetitionDetail.weatherModel?.let {
                    WeatherCard(
                        weatherModel = it,
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
                imageUrl = competitionDetail.imageUrl!!,
                competitionTime = competitionDetail.selectedTime,
                competitionDate = competitionDetail.selectedDate,
                locationName = competitionDetail.locationName!!,
                weatherModel = sentWeatherModel
            )
            Button(onClick = {
                competitionDetailViewModel.saveCompetition(savedCompetition)
                navController.navigate(NavigationGraph.SAVED_COMPETITIONS.route)
            }) {
                Text(text = "Save Competition")
            }
        }
    }
}







