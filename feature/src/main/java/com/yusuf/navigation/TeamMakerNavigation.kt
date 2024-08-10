    package com.yusuf.navigation

    import androidx.compose.foundation.layout.Box
    import androidx.compose.foundation.layout.fillMaxSize
    import androidx.compose.runtime.Composable
    import androidx.compose.runtime.LaunchedEffect
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.hilt.navigation.compose.hiltViewModel
    import androidx.navigation.NavHostController
    import androidx.navigation.compose.NavHost
    import androidx.navigation.compose.composable
    import androidx.compose.runtime.key
    import androidx.navigation.NavType
    import androidx.navigation.navArgument
    import com.google.gson.Gson
    import com.yusuf.component.LoadingLottie
    import com.yusuf.domain.model.competition_detail.CompetitionDetail
    import com.yusuf.domain.model.firebase.SavedCompetitionsModel
    import com.yusuf.feature.R
    import com.yusuf.feature.player_list.PlayerListScreen
    import com.yusuf.feature.auth.forgot_password.ForgotPasswordScreen
    import com.yusuf.feature.create_competition.CreateCompetitionScreen
    import com.yusuf.feature.home.ChooseCompetitionTypeScreen
    import com.yusuf.feature.auth.login.LoginScreen
    import com.yusuf.feature.auth.register.RegisterScreen
    import com.yusuf.feature.onboarding.OnBoardingScreen
    import com.yusuf.feature.options.OptionsScreen
    import com.yusuf.feature.splash_screen.SplashScreen
    import com.yusuf.feature.competition_detail.CompetitionDetailScreen
    import com.yusuf.feature.saved_competitions.SavedCompetitionsScreen
    import com.yusuf.navigation.main_viewmodel.MainViewModel
    import com.yusuf.utils.default_competition.Competition

    @Composable
    fun TeamMakerNavigation(
        navController: NavHostController,
        mainViewModel: MainViewModel = hiltViewModel(),
        onTitleChange: (String) -> Unit,
        key: Int
        ) {

        key(key) {
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
        } else {
            NavHost(
                navController = navController,
                startDestination = mainViewModel.startDestination
            ) {
                composable(NavigationGraph.ONBOARDING_SCREEN.route) {
                    OnBoardingScreen(mainViewModel, navController)
                }
                composable(NavigationGraph.LOGIN.route) {
                    LoginScreen(navController)
                }
                composable(NavigationGraph.REGISTER.route) {
                    RegisterScreen(navController)
                }
                composable(NavigationGraph.FORGOT_PASSWORD.route) {
                    ForgotPasswordScreen(navController)
                }
                composable(NavigationGraph.CHOOSE_COMPETITION_TYPE.route) {
                    ChooseCompetitionTypeScreen(navController)
                    onTitleChange("Choose Competition Type")
                }
                composable(
                    route = NavigationGraph.OPTIONS.route,
                    arguments = listOf(navArgument("competitionJson") { type = NavType.StringType })
                ) { backStackEntry ->
                    val gson = Gson()
                    val competitionJson = backStackEntry.arguments?.getString("competitionJson")
                    val competition = gson.fromJson(competitionJson, Competition::class.java)
                    OptionsScreen(
                        navController = navController,
                        competition = competition
                    )
                    onTitleChange("Options")
                }

                composable(
                    route = NavigationGraph.COMPETITION_DETAIL.route,
                    arguments = listOf(
                        navArgument("competitionDetailJson") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val gson = Gson()
                    val competitionDetailJson =
                        backStackEntry.arguments?.getString("competitionDetailJson")
                    val competitionDetail =
                        gson.fromJson(competitionDetailJson, CompetitionDetail::class.java)
                    CompetitionDetailScreen(
                        navController = navController,
                        competitionDetail = competitionDetail
                    )
                    onTitleChange("Competition Detail")
                }

                composable(NavigationGraph.SAVED_COMPETITION_DETAIL.route,
                    arguments = listOf(
                        navArgument("savedCompetitionDetailJson") { type = NavType.StringType }
                    )
                ) { backStackEntry ->
                    val gson = Gson()
                    val savedCompetitionDetailJson =
                        backStackEntry.arguments?.getString("savedCompetitionDetailJson")
                    val savedCompetitionDetail = gson.fromJson(
                        savedCompetitionDetailJson,
                        SavedCompetitionsModel::class.java
                    )
                    CompetitionDetailScreen(
                        navController = navController,
                        savedCompetitionDetail = savedCompetitionDetail
                    )
                    onTitleChange("Competition Detail")
                }

                composable(NavigationGraph.PLAYER_LIST.route) {
                    PlayerListScreen()
                    onTitleChange("Player List")
                }

                composable(NavigationGraph.CREATE_COMPETITION.route) {
                    CreateCompetitionScreen(navController)
                    onTitleChange("Create Competition")
                }

                composable(NavigationGraph.SAVED_COMPETITIONS.route) {
                    SavedCompetitionsScreen(navController)
                    onTitleChange("Saved Competitions")
                }
            }
        }
    }
    }