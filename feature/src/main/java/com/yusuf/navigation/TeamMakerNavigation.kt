package com.yusuf.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yusuf.feature.add_player.AddPlayerScreen
import com.yusuf.feature.create_match.CreateMatchScreen
import com.yusuf.feature.home.ChooseSportScreen
import com.yusuf.feature.options.OptionsScreen
import com.yusuf.feature.player_list.PlayerListScreen

@Composable
fun TeamMakerNavigation() {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "choose_sport") {

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