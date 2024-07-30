package com.yusuf.feature.options

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.yusuf.feature.R
import com.yusuf.navigation.NavigationGraph

@Composable
fun OptionsScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        OptionsCard(navController, "Players", R.drawable.players, NavigationGraph.ADD_PLAYER.route)
        OptionsCard(navController, "Create a Match", R.drawable.createamatch, NavigationGraph.CREATE_MATCH.route)
    }
}


@Composable
fun OptionsCard(
    navController: NavController,
    sportName: String,
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
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(16.dp)
        ) {
            Image(painter = painterResource(id = imageResourceId), contentDescription = null)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = sportName, style = MaterialTheme.typography.bodyMedium)
            ElevatedButton(onClick = { navController.navigate(route) }) {
                Text(text = "Go to $sportName")
            }
        }
    }
}


@Preview
@Composable
fun OptionsScreenPreview() {
    val navController = rememberNavController()
    OptionsScreen(navController)
}