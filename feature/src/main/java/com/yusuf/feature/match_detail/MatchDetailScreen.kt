package com.yusuf.feature.match_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yusuf.feature.create_match.TimePicker
import com.yusuf.feature.create_match.location.LocationScreen
import com.yusuf.feature.create_match.weather.Weather

@Composable
fun MatchDetailScreen() {
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