package com.yusuf.feature.main_content

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.yusuf.feature.R
import com.yusuf.feature.auth.login.viewmodel.LoginViewModel
import com.yusuf.navigation.NavigationGraph
import com.yusuf.navigation.TeamMakerNavigation
import com.yusuf.theme.APPBAR_GREEN

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(navController: NavHostController, loginViewModel: LoginViewModel = hiltViewModel()) {
    var appBarTitle by remember { mutableStateOf<String?>(null) }
    var showDialog by remember { mutableStateOf(false) }
    var navigationKey by remember { mutableStateOf(0) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Logout") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    loginViewModel.signOut()
                    navController.navigate(NavigationGraph.LOGIN.route) {
                        popUpTo(NavigationGraph.LOGIN.route) {
                            inclusive = true
                        }
                    }
                    appBarTitle = null
                    navigationKey++
                }) {
                    Text("Yes")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Hayır")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            appBarTitle?.let { title ->
                TopAppBar(
                    title = {
                        Text(
                            text = title,
                            style = TextStyle(
                                color = APPBAR_GREEN,
                                fontSize = 25.sp
                            ),
                            fontFamily = FontFamily(Font(R.font.onboarding_title1, FontWeight.Normal))
                        )
                    },
                    actions = {
                        if (title == "Choose Competition Type") {
                            IconButton(onClick = { showDialog = true }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_logout),
                                    contentDescription = "Logout",
                                    tint = APPBAR_GREEN,
                                    modifier = Modifier.size(30.dp))
                            }
                        } else {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(
                                    painterResource(R.drawable.ic_arrow_right),
                                    contentDescription = "Back",
                                    tint = APPBAR_GREEN,
                                    modifier = Modifier.size(30.dp))
                            }
                        }
                    }
                )
            }
        },
        content = { innerPadding ->
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
    )
}
