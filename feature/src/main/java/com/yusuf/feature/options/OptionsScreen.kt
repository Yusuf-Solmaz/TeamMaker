package com.yusuf.feature.options

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import com.yusuf.feature.R
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.Green
import com.yusuf.utils.Competition


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
    text:String,
    navController: NavController,
    imageResourceId: Int,
    route: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate(route) },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = imageResourceId),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            Modifier.fillMaxWidth(),

            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically){
            Button(
                onClick = {
                    navController.navigate(route)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Green,
                    contentColor = Color.White
                )
            ){
                Text(text = text)
            }
        }

    }
}
