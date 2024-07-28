package com.yusuf.feature.options

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun OptionsScreen(navController: NavController) {
    Column(
        modifier =
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ElevatedButton(onClick = { /*TODO*/ }) {
            Text(text = "Players")
        }
        ElevatedButton(onClick = { /*TODO*/ }) {
            Text(text = "Create a Match")

        }

    }

}


@Preview
@Composable
fun OptionsScreenPreview() {
    val navController = rememberNavController()
    OptionsScreen(navController)
}