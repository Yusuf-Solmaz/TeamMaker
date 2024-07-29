package com.yusuf.feature.add_player

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yusuf.component.LoadingLottie
import com.yusuf.feature.R

@Composable
fun AddPlayerScreen(navController: NavController){
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {

        LoadingLottie(resId = R.raw.football_team_anim)

        Button(onClick = {
            navController.navigate("choose_sport")
        }) {
            Text(text = "Go to Choose Sport Page")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddPlayerScreenPreview() {
    AddPlayerScreen(navController = rememberNavController())
}