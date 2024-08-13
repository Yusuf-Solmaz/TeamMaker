package com.yusuf.feature.auth.forgot_password.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.use_cases.firebase_use_cases.auth.ResetPasswordUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.auth.forgot_password.state.ForgotPasswordState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState

    internal fun sendPasswordResetEmail(email: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            resetPasswordUseCase(email).collect{
                    result ->
                when(result){
                    is RootResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is RootResult.Success -> {
                        _uiState.value = _uiState.value.copy(transaction = true, isLoading = false)
                    }
                    is RootResult.Error -> {
                        _uiState.value = _uiState.value.copy(error = result.message, isLoading = false, transaction = false)
                    }
                }
            }
        }
    }
}