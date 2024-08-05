package com.yusuf.feature.create_competition.select_player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.domain.model.firebase.PlayerData

@Composable
fun BackCardContent(player: PlayerData, onFlip: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White.copy(alpha = 0.8f), RoundedCornerShape(12.dp))
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Speed: ${player.speed}", fontSize = 16.sp)
            Text(text = "Focus: ${player.focus}", fontSize = 16.sp)
            Text(text = "Condition: ${player.condition}", fontSize = 16.sp)
            Text(text = "Durability: ${player.durability}", fontSize = 16.sp)
        }
    }
}