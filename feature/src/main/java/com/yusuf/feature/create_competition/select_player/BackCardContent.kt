package com.yusuf.feature.create_competition.select_player

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.feature.R
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource

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
                .align(Alignment.Center)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            AttributeRow(icon = ImageVector.vectorResource(id = R.drawable.run), label = "SPE", value = player.speed)
            AttributeRow(icon = ImageVector.vectorResource(id = R.drawable.focus_weak), label = "FOC", value = player.focus)
            AttributeRow(icon = ImageVector.vectorResource(id = R.drawable.face), label = "CON", value = player.condition)
            AttributeRow(icon = ImageVector.vectorResource(id = R.drawable.durability), label = "DUR", value = player.durability)
        }
    }
}

@Composable
fun AttributeRow(icon: ImageVector, label: String, value: Int) {
    val textColor = if (value < 5) Color.Red else Color.Green

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp) // Add vertical padding between rows
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "$label Icon",
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp)) // Add spacing between icon and text
        Text(
            text = "$label: $value",
            fontSize = 16.sp,
            color = textColor,
            modifier = Modifier.weight(1f) // Make text take up the remaining space
        )
    }
}

