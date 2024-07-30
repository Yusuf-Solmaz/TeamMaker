package com.yusuf.feature.auth.login.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.use_cases.firebase_use_cases.auth.IsLoggedInUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.auth.SignInUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.auth.SignOutUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.auth.login.state.IsLoggedInState
import com.yusuf.feature.auth.login.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val signOutUseCase: SignOutUseCase,
    private val isLoggedInUseCase: IsLoggedInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _loggingState = MutableStateFlow(IsLoggedInState())
    val loggingState: StateFlow<IsLoggedInState> = _loggingState

    fun signIn(email: String, password: String) {
        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(isLoading = true)
            signInUseCase(email, password).collect { result ->
                when (result) {
                    is RootResult.Success -> {
                        result.data?.let { user ->
                            Log.d("LoginViewModel", "User: $user")
                            _uiState.value = _uiState.value.copy(user = user, isLoading = false)
                        }
                    }

                    is RootResult.Error -> {
                        Log.d("LoginViewModel", "Error: ${result.message}")
                        _uiState.value =
                            _uiState.value.copy(error = result.message, isLoading = false)
                    }

                    RootResult.Loading -> {
                        Log.d("LoginViewModel", "Loading")
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }



    fun signOut() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            signOutUseCase().collect { result ->
                when (result) {
                    is RootResult.Success -> {
                        _uiState.value = _uiState.value.copy(
                            user = null,
                            isLoading = false
                        )
                    }
                    is RootResult.Error -> {
                        _uiState.value =
                            _uiState.value.copy(error = result.message, isLoading = false)
                    }

                    RootResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }


    fun isLoggedIn() {
        viewModelScope.launch {
            isLoggedInUseCase().collect { isLoggedIn ->
                _loggingState.value = _loggingState.value.copy(isLoading = true)
                when (isLoggedIn) {
                    is RootResult.Success -> {
                        _loggingState.value = _loggingState.value.copy(
                            isLoading = false,
                            transaction = isLoggedIn.data ?: false
                        )
                    }
                    is RootResult.Error -> {
                        _loggingState.value = _loggingState.value.copy(
                            isLoading = false,
                            error = isLoggedIn.message
                        )
                    }
                    RootResult.Loading -> {
                        _loggingState.value = _loggingState.value.copy(isLoading = true)
                    }
                }
            }
        }
    }
}