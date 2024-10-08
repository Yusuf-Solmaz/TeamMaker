package com.yusuf.feature.main_content

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.yusuf.component.LoadingLottie
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.feature.R
import com.yusuf.feature.auth.login.viewmodel.LoginViewModel
import com.yusuf.feature.main_content.viewmodel.MainContentViewModel
import com.yusuf.navigation.NavigationGraph
import com.yusuf.navigation.TeamMakerNavigation
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.DARK_RED
import com.yusuf.theme.RED

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    navController: NavHostController,
    loginViewModel: LoginViewModel = hiltViewModel(),
    mainContentViewModel: MainContentViewModel = hiltViewModel()
) {
    val deleteUserState by mainContentViewModel.deleteUserState.collectAsState()
    val uiState by loginViewModel.uiState.collectAsState()
    val isLoggedInState by loginViewModel.loggingState.collectAsState()

    var appBarTitle by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogAction by remember { mutableStateOf("") }
    var navigationKey by remember { mutableIntStateOf(0) }
    val context = LocalContext.current

    LaunchedEffect(true) {
        loginViewModel.isLoggedIn()

        Log.i("MainScreen", "isLoggedInState.isAnonymous: ${isLoggedInState.isAnonymous}")
    }

    LaunchedEffect(deleteUserState.transaction) {
        Log.i("MainScreen", "deleteUserState.transaction: ${deleteUserState.transaction}")
        if (deleteUserState.transaction) {
            goToLogin(
                loginViewModel = loginViewModel,
                navHostController = navController
            )
            appBarTitle = null
            navigationKey++
            Toast.makeText(context, "User deleted successfully", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDialog) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = { showDialog = false },
            title = {
                Text(
                    text = if (dialogAction == "Logout") "Logout" else "Delete Account",
                    color = Color.Black
                )
            },
            text = {
                Text(
                    "Are you sure you want to ${dialogAction.lowercase()}?",
                    color = Color.Black
                )
            },
            confirmButton = {
                AuthButtonComponent(
                    value = "Yes",
                    onClick = {
                        showDialog = false
                        when (dialogAction) {
                            "Logout" -> {
                                goToLogin(
                                    loginViewModel = loginViewModel,
                                    navHostController = navController
                                )
                                appBarTitle = null
                                navigationKey++
                            }

                            "Delete Account" -> {
                                mainContentViewModel.deleteUser()
                            }

                            "Forgot Me" -> {
                                mainContentViewModel.deleteUser()
                            }
                        }
                    },
                    modifier = Modifier.width(60.dp),
                    fillMaxWidth = false,
                    heightIn = 35.dp,
                    firstColor = RED,
                    secondColor = DARK_RED
                )
            },
            dismissButton = {
                AuthButtonComponent(
                    value = "No",
                    onClick = { showDialog = false },
                    modifier = Modifier.width(60.dp),
                    fillMaxWidth = false,
                    heightIn = 35.dp
                )
            }
        )
    }

    Scaffold(
        topBar = {
            appBarTitle?.let { title ->
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = TextStyle(
                                color = APPBAR_GREEN,
                                fontSize = 25.sp
                            ),
                            fontFamily = FontFamily(
                                Font(
                                    R.font.onboarding_title1,
                                    FontWeight.Normal
                                )
                            )
                        )
                    },
                    navigationIcon = {
                        if (title != "Choose Competition Type") {
                            IconButton(onClick = {
                                if (title == "Saved Competitions") {
                                    navController.navigate(NavigationGraph.CHOOSE_COMPETITION_TYPE.route)
                                } else {
                                    navController.navigateUp()
                                }
                            }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_arrow_left),
                                    contentDescription = "Back",
                                    tint = APPBAR_GREEN,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                        }
                    },
                    actions = {
                        if (title == "Choose Competition Type") {
                            var expanded by remember { mutableStateOf(false) }
                            IconButton(onClick = {
                                expanded = !expanded
                            }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_vert),
                                    contentDescription = "Settings",
                                    tint = APPBAR_GREEN,
                                    modifier = Modifier.size(30.dp)
                                )
                            }
                            MaterialTheme(
                                shapes = MaterialTheme.shapes.copy(
                                    extraSmall = RoundedCornerShape(
                                        16.dp
                                    )
                                )
                            ) {
                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    Modifier.background(Color.White)
                                ) {

                                    DropdownMenuItem(
                                        text = { Text("Saved Competitions", color = Color.Black) },
                                        onClick = {
                                            expanded = false
                                            navController.navigate(NavigationGraph.SAVED_COMPETITIONS.route)
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                if (isLoggedInState.isAnonymous) "Forgot Me" else "Delete Account",
                                                color = RED
                                            )
                                        },
                                        onClick = {
                                            expanded = false
                                            dialogAction = "Delete Account"
                                            showDialog = true
                                        }
                                    )
                                    DropdownMenuItem(
                                        text = { Text("Logout", color = Color.Black) },
                                        onClick = {
                                            expanded = false
                                            dialogAction = "Logout"
                                            showDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                )
            }
        },
        content = { innerPadding ->

            if (deleteUserState.isLoading || uiState.isLoading) {
                Column(
                    Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    LoadingLottie(R.raw.loading_anim)
                }
            } else {
                Column(
                    Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    TeamMakerNavigation(
                        navController = navController,
                        onTitleChange = { newTitle ->
                            appBarTitle = newTitle
                        },
                        key = navigationKey
                    )
                }
            }
        }
    )
}

private fun goToLogin(loginViewModel: LoginViewModel, navHostController: NavHostController) {
    loginViewModel.signOut()
    navHostController.navigate(NavigationGraph.LOGIN.route) {
        popUpTo(NavigationGraph.LOGIN.route) {
            inclusive = true
        }
    }
}

