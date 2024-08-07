package com.yusuf.feature.options

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.feature.R
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.APPBAR_GREEN
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
            text = "Add Player",
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
            .padding(8.dp)
            .clickable { navController.navigate(route) },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
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
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                AuthButtonComponent(
                    onClick = { navController.navigate(route) },
                    value = text,
                    modifier = Modifier
                        .heightIn(24.dp)
                        .width(120.dp)
                )
            }
        }
    }
}
