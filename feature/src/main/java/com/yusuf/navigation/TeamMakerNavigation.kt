package com.yusuf.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yusuf.feature.add_player.AddPlayerScreen
import com.yusuf.feature.home.HomeScreen

@Composable
fun TeamMakerNavigation(){

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home"){
        composable("home"){
            HomeScreen(navController)
        }
        composable("add_player"){
            AddPlayerScreen(navController)
        }
    }

}