package com.yusuf.feature.auth.is_logged_in

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yusuf.component.LoadingLottie
import com.yusuf.feature.R
import com.yusuf.feature.auth.login.viewmodel.LoginViewModel
import com.yusuf.navigation.NavigationGraph

@Composable
fun IsLoggedIn(navController: NavController,viewModel: LoginViewModel= hiltViewModel()){

    val loggingState by viewModel.loggingState.collectAsState()

    LaunchedEffect(true) {
        viewModel.isLoggedIn()
    }

    LaunchedEffect(loggingState.transaction) {
        if (loggingState.transaction) {
            navController.navigate(NavigationGraph.CHOOSE_COMPETITION_TYPE.route){
                popUpTo(NavigationGraph.CHOOSE_COMPETITION_TYPE.route){
                    inclusive = true
                }
            }
        }
        else{
            navController.navigate(NavigationGraph.LOGIN.route){
                popUpTo(NavigationGraph.LOGIN.route){
                    inclusive = true
                }
            }
        }
    }

    if (loggingState.isLoading){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoadingLottie(R.raw.loading_anim)
        }
    }

}