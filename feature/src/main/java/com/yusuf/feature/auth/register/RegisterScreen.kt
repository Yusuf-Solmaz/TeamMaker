package com.yusuf.feature.auth.register

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.yusuf.component.DividerTextComponent
import com.yusuf.component.HeadingTextComponent
import com.yusuf.component.LoadingLottie
import com.yusuf.component.TextFieldComponent
import com.yusuf.component.auth_components.AuthButtonComponent
import com.yusuf.component.auth_components.ClickableLoginTextComponent
import com.yusuf.component.auth_components.PasswordFieldComponent
import com.yusuf.feature.R
import com.yusuf.feature.auth.register.viewmodel.RegisterViewModel
import com.yusuf.navigation.NavigationGraph

@Composable
fun RegisterScreen(
    navController: NavController,
    onLoginClick: () -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    val context = LocalContext.current


    LaunchedEffect(uiState.error) {
        uiState.error?.let { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }


    LaunchedEffect(uiState.user) {
        uiState.user?.let {
            navController.navigate(NavigationGraph.LOGIN.route) {
                popUpTo(NavigationGraph.LOGIN.route) {
                    inclusive = true }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(28.dp)
    ) {
        if (uiState.isLoading) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,

                ) {
                LoadingLottie(resId = R.raw.loading_anim)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

                Spacer(modifier = Modifier.height(20.dp))
                HeadingTextComponent("Create an Account")
                Spacer(modifier = Modifier.height(20.dp))


                TextFieldComponent(
                    stateValue = email,
                    onValueChange = { updatedEmail ->
                        email = updatedEmail.trim()
                    },
                    label = "Email",
                    painterResource = painterResource(id = R.drawable.mail_icon)
                )

                PasswordFieldComponent(
                    stateValue = password,
                    onValueChange = { updatedPassword ->
                        password = updatedPassword.trim()
                    },
                    label = "Password",
                    painterResource = painterResource(id = R.drawable.ic_lock)
                )

                Spacer(modifier = Modifier.height(70.dp))
                AuthButtonComponent(value = "Register", onClick = {
                    viewModel.signUp(email, password)
                })
                Spacer(modifier = Modifier.height(20.dp))

                DividerTextComponent()

                ClickableLoginTextComponent(tryToLogin = true, onTextSelected = {
                    onLoginClick()
                    navController.navigate(NavigationGraph.LOGIN.route)
                })
            }
        }
    }
}
