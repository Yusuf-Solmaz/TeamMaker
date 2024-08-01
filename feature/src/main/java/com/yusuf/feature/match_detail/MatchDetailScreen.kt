package com.yusuf.feature.match_detail

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import com.yusuf.feature.create_match.TimePicker
import com.yusuf.feature.create_match.location.LocationScreen
import com.yusuf.feature.create_match.weather.Weather

@Composable
fun MatchDetailScreen(sharedViewModel: SharedViewModel = hiltViewModel()) {
    val uiState by sharedViewModel.uiState.collectAsState()

    Log.d("MatchDetailScreen", "Balanced teams: ${uiState.teams}")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimePicker()
        Spacer(modifier = Modifier.height(2.dp))
        LocationScreen()
        Spacer(modifier = Modifier.height(2.dp))
        Weather()
        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.isLoading) {
            Text("Loading...", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        } else if (uiState.errorMessage != null) {
            Text("Error: ${uiState.errorMessage}", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        } else {
            Text("Team 1", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.teams?.first?.size ?: 0) { index ->
                    val player = uiState.teams?.first?.get(index)
                    PlayerCard(player)
                }
            }

            Text("Team 2", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(uiState.teams?.second?.size ?: 0) { index ->
                    val player = uiState.teams?.second?.get(index)
                    PlayerCard(player)
                }
            }
        }
    }
}

@Composable
fun PlayerCard(player: PlayerData?) {
    Card(
        modifier = Modifier
            .background(Color.Transparent)
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier.background(Color.White)
        ) {
            Image(
                painter = painterResource(id = R.drawable.player_card),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.players),
                    contentDescription = "${player?.firstName} ${player?.lastName}",
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )
                Text(
                    text = "${player?.firstName} ${player?.lastName}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column {
                        Text(text = "FOC: ${player?.skillRating}", fontSize = 12.sp)
                        Text(text = "CON: ${player?.skillRating}", fontSize = 12.sp)
                    }
                    Column {
                        Text(text = "SPE: ${player?.skillRating}", fontSize = 12.sp)
                        Text(text = "DUR: ${player?.skillRating}", fontSize = 12.sp)
                    }
                }
            }
            Text(
                text = player?.skillRating.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(start = 20.dp, top = 40.dp)
                    .border(1.dp, Color.Black, RoundedCornerShape(4.dp))
                    .padding(4.dp)
            )
        }
    }
}