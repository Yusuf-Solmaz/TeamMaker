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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yusuf.domain.model.competition_detail.CompetitionDetail
import com.yusuf.feature.create_competition.location.LocationScreen
import com.yusuf.feature.competition_detail.weather.Weather

@Composable
fun CompetitionDetailScreen(
    navController: NavController,
    competitionDetail: CompetitionDetail
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = competitionDetail.selectedTime,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = competitionDetail.selectedDate,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Log.d("MatchDetailScreen", "CompetitionDetail: $competitionDetail")

            Spacer(modifier = Modifier.height(2.dp))

            Weather(
                location = competitionDetail.location!!,
                locationName = competitionDetail.locationName!!
            )

            Spacer(modifier = Modifier.height(16.dp))

            TeamListScreen(
                firstTeam = competitionDetail.firstBalancedTeam,
                secondTeam = competitionDetail.secondBalancedTeam
            )
        }
    }
}






