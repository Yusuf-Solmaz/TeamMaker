package com.yusuf.feature.splash_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.component.LoadingLottie
import com.yusuf.feature.R
import com.yusuf.theme.APPBAR_GREEN

@Composable
fun SplashScreen() {

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {

        LoadingLottie(R.raw.splash_screen_anim)

        Spacer(modifier = Modifier.height(40.dp))

        Text(text = "Team Maker", style = TextStyle(
            color = APPBAR_GREEN,
            fontSize = 50.sp,
            fontFamily = FontFamily(Font(R.font.splash_title_font)),
        ))
    }

}