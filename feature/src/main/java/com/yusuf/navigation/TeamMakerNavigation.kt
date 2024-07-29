package com.yusuf.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yusuf.component.LoadingLottie
import com.yusuf.feature.R
import com.yusuf.feature.add_player.AddPlayerScreen
import com.yusuf.feature.create_match.CreateMatchScreen
import com.yusuf.feature.home.ChooseSportScreen
import com.yusuf.feature.onboarding.OnBoardingScreen
import com.yusuf.feature.options.OptionsScreen
import com.yusuf.feature.player_list.PlayerListScreen
import com.yusuf.feature.splash_screen.SplashScreen
import com.yusuf.navigation.main_viewmodel.MainViewModel

@Composable
fun TeamMakerNavigation(mainViewModel: MainViewModel) {

    val navController = rememberNavController()

    LaunchedEffect(mainViewModel.isLoading) {
        if (!mainViewModel.isLoading) {
            navController.navigate(mainViewModel.startDestination) {
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    if (mainViewModel.isSplashScreenVisible) {
        SplashScreen()
    } else if (mainViewModel.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            LoadingLottie(resId = R.raw.loading_anim)
        }
    }

    else{
        NavHost(navController = navController, startDestination = mainViewModel.startDestination) {

            composable("onboarding_screen") {
                OnBoardingScreen(mainViewModel, navController)
            }

            composable("choose_sport") {
                ChooseSportScreen(navController)
            }
            composable("options") {
                OptionsScreen(navController)
            }
            composable("players_list") {
                PlayerListScreen(navController)
            }
            composable("create_match") {
                CreateMatchScreen(navController)
            }
            composable("add_player") {
                AddPlayerScreen(navController)
            }
        }
    }
}