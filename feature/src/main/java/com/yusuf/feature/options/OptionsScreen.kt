package com.yusuf.feature.options

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.yusuf.feature.R
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.DARK_GREEN
import com.yusuf.utils.default_competition.Competition


@Composable
fun OptionsScreen(
    navController: NavController,
    competition: Competition
) {
    Log.d("OptionsScreen", "Competition: $competition")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        OptionsCard(
            text = "Players",
            navController = navController,
            imageResourceId = if (competition.competitionFirstImage != 0) competition.competitionFirstImage else R.drawable.players,
            route = NavigationGraph.PLAYER_LIST.route
        )

        OptionsCard(
            text = "Create Match",
            navController = navController,
            imageResourceId = if (competition.competitionTeamImage != 0) competition.competitionTeamImage else R.drawable.createamatch,
            route = NavigationGraph.CREATE_COMPETITION.route
        )
    }
}


@Composable
fun OptionsCard(
    text: String,
    navController: NavController,
    imageResourceId: Int,
    route: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate(route)
                }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(170.dp),
                    painter = painterResource(id = imageResourceId),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )

                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(width = 180.dp, height = 50.dp),
                    )
                    {
                        Button(
                            onClick = { navController.navigate(route) },
                            modifier = Modifier.fillMaxSize(),
                            colors = ButtonDefaults.buttonColors(Color.White)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        brush = Brush.verticalGradient(
                                            listOf(
                                                APPBAR_GREEN,
                                                DARK_GREEN
                                            )
                                        ),
                                        shape = RoundedCornerShape(50.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = text,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
