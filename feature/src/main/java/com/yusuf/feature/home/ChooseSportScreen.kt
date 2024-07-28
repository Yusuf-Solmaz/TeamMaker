package com.yusuf.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yusuf.feature.R

@Composable
fun ChooseSportScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(painter = painterResource(id = R.drawable.example_image), contentDescription = null)
        Button(onClick = {
            navController.navigate("add_player")
        })
        {
            Text(text = "Go to Other Page")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseSportScreenPreview() {
    val navController = rememberNavController()
    ChooseSportScreen(navController)
}