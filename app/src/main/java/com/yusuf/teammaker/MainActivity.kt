package com.yusuf.teammaker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.yusuf.feature.home.HomeScreen
import com.yusuf.navigation.TeamMakerNavigation
import com.yusuf.teammaker.ui.theme.TeamMakerTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamMakerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        TeamMakerNavigation()
                    })
            }
        }
    }
}
