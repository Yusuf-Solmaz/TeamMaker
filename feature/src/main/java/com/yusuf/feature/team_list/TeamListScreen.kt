package com.yusuf.feature.team_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.feature.create_match.TimePicker
import com.yusuf.feature.create_match.location.LocationScreen
import com.yusuf.feature.create_match.weather.Weather

@Composable
fun TeamListScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimePicker()
        Spacer(modifier = Modifier.height(2.dp))
        LocationScreen()
        Spacer(modifier = Modifier.height(2.dp))
        Weather()
    }
}