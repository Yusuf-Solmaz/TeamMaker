package com.yusuf.teammaker

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.yusuf.navigation.TeamMakerNavigation
import com.yusuf.navigation.main_viewmodel.MainViewModel
import com.yusuf.teammaker.ui.theme.TeamMakerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel by viewModels<MainViewModel>()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TeamMakerTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = {
                        TeamMakerNavigation(mainViewModel = mainViewModel)
                    })
            }
        }
    }
}
