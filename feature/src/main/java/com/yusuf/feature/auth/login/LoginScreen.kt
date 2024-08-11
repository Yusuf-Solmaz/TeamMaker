package com.yusuf.feature.auth.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.component.DividerTextComponent
import com.yusuf.component.LoadingLottie
import com.yusuf.component.auth_components.PasswordFieldComponent
import com.yusuf.component.TextFieldComponent
import com.yusuf.component.UnderLinedTextComponent
import com.yusuf.component.auth_components.ClickableLoginTextComponent
import com.yusuf.feature.R
import com.yusuf.feature.auth.login.viewmodel.LoginViewModel
import com.yusuf.navigation.NavigationGraph
import com.yusuf.theme.APPBAR_GREEN
import com.yusuf.theme.GREEN

@Composable
fun LoginScreen(
    navController: NavHostController,
    onSignUpClick: () -> Unit = {},
    viewModel: LoginViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState.user) {
        uiState.user?.let {
            navController.navigate(NavigationGraph.CHOOSE_COMPETITION_TYPE.route) {
                popUpTo(NavigationGraph.CHOOSE_COMPETITION_TYPE.route) {
                    inclusive = true
                }
            }
        }
    }


    if (uiState.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            LoadingLottie(resId = R.raw.loading_anim)
        }
    } else {
        Scaffold(modifier = Modifier.padding(18.dp)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                LoadingLottie(resId = R.raw.football_team_anim, height = 250.dp)

                Spacer(modifier = Modifier.height(10.dp))
                TextFieldComponent(
                    email,
                    onValueChange = { updatedEmail -> email = updatedEmail.trim() },
                    label = "Email",
                    painterResource = painterResource(id = R.drawable.mail_icon)
                )
                PasswordFieldComponent(
                    password,
                    label = "Password",
                    onValueChange = { updatedPassword -> password = updatedPassword.trim() },
                    painterResource(id = R.drawable.ic_lock)
                )
                UnderLinedTextComponent(value = "Forgot your password?", onClick = {
                    navController.navigate(NavigationGraph.FORGOT_PASSWORD.route)
                })
                Spacer(modifier = Modifier.height(10.dp))
                AuthButtonComponent(value = "Login", onClick = {
                    viewModel.signIn(email, password)
                })
                Spacer(modifier = Modifier.height(10.dp))
                Spacer(modifier = Modifier.height(15.dp))
                ClickableLoginTextComponent(tryToLogin = false, onTextSelected = {
                    onSignUpClick()
                    navController.navigate(NavigationGraph.REGISTER.route)
                })
                DividerTextComponent()

                Text(
                    text = "Login as Guest",
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = listOf(APPBAR_GREEN, GREEN)
                        ),
                        fontSize = 23.sp,
                        fontFamily = FontFamily(Font(R.font.splash_title_font))
                    ),
                    modifier = Modifier.clickable {
                        viewModel.signInAnonymously()
                    }
                )
            }
        }
    }
}