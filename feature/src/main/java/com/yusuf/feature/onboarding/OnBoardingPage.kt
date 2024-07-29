package com.yusuf.feature.onboarding

import Page
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yusuf.component.LoadingLottie
import com.yusuf.feature.R
import com.yusuf.theme.Blue

@Composable
fun OnboardingPage(page: Page) {
    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        LoadingLottie(resId = page.lottieFile)
        Text(
            text = page.title,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.onboarding_title1)),
                fontSize = 35.sp,
                color = Blue
            ),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )
        Text(
            text = page.description,
            style = TextStyle(
                fontFamily = FontFamily(Font(R.font.onboarding_content)),
                fontSize = 18.sp
            ),
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 2.dp)
        )
    }
}