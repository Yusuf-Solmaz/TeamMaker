package com.yusuf.feature.home

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

@Composable
fun ChooseSportScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        SportCard(navController, "Football", R.drawable.football)
        SportCard(navController, "Volleyball", R.drawable.volleyball)
    }
}

@Composable
fun SportCard(navController: NavController, sportName: String, imageResourceId: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { navController.navigate("options") },
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
            ElevatedButton(onClick = { navController.navigate("options") }) {
                Text(text = "Go to $sportName")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChooseSportScreenPreview() {
    val navController = rememberNavController()
    ChooseSportScreen(navController)
}
