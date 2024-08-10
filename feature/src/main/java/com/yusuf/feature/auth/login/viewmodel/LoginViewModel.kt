package com.yusuf.feature.auth.login.viewmodel


import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.use_cases.firebase_use_cases.auth.IsLoggedInUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.auth.SignInAnonymously
import com.yusuf.domain.use_cases.firebase_use_cases.auth.SignInUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.auth.SignOutUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.AddCompetitionUseCase
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
    private val isLoggedInUseCase: IsLoggedInUseCase,
    private val singInAnonymouslyUseCase: SignInAnonymously,
    private val addCompetitionUseCase: AddCompetitionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    private val _loggingState = MutableStateFlow(IsLoggedInState())
    val loggingState: StateFlow<IsLoggedInState> = _loggingState

    fun signInAnonymously(){
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            singInAnonymouslyUseCase().collect { result ->

                when (result) {
                    is RootResult.Error -> {
                        _uiState.value =
                            _uiState.value.copy(error = result.message, isLoading = false)
                    }
                    RootResult.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true)
                    }
                    is RootResult.Success ->{
                        result.data?.let { user ->
                            Log.d("LoginViewModelAnonymously", "User: $user")
                            runAddDefaultCompetitions(addCompetitionUseCase)
                            _uiState.value = _uiState.value.copy(user = user, isLoading = false, isLoggedInAnonymously = true)
                        }
                    }
                }

            }
        }
    }

    private suspend fun runAddDefaultCompetitions(addCompetitionUseCase: AddCompetitionUseCase) {
        addDefaultCompetitions(addCompetitionUseCase, CompetitionData(competitionName = "Football", competitionImageUrl = "https://firebasestorage.googleapis.com/v0/b/teammaker-a3739.appspot.com/o/competitions%2Ffootball.jpg?alt=media&token=97c1b88e-b1c4-46df-b591-d41a2a0789cd"))
        addDefaultCompetitions(addCompetitionUseCase, CompetitionData( competitionName = "Basketball", competitionImageUrl ="https://firebasestorage.googleapis.com/v0/b/teammaker-a3739.appspot.com/o/competitions%2Fbasketball.jpg?alt=media&token=a0543f32-bfbc-4e66-b9d4-79c171775db8"))

    }

    private suspend fun addDefaultCompetitions(addCompetitionUseCase: AddCompetitionUseCase, competition: CompetitionData) {
        try {
            addCompetitionUseCase(
                CompetitionData(
                    competitionName = competition.competitionName,
                    competitionImageUrl = competition.competitionImageUrl
                )
            ).collect { result ->
                when (result) {
                    is RootResult.Success -> {
                        Log.d("RegisterViewModel", "Competition added successfully")
                    }
                    is RootResult.Error -> {
                        Log.e("RegisterViewModel", "Error adding competition: ${result.message}")
                    }
                    is RootResult.Loading -> {
                        Log.d("RegisterViewModel", "Adding competition in progress")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("RegisterViewModel", "Error in addDefaultCompetitions: ${e.message}")
        }
    }

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

                        val currentUser = FirebaseAuth.getInstance().currentUser
                        val isAnonymous = currentUser?.isAnonymous ?: false

                        _loggingState.value = _loggingState.value.copy(
                            isLoading = false,
                            transaction = isLoggedIn.data ?: false,
                            isAnonymous = isAnonymous
                        )
                        Log.d("LoginViewModel", "Is Logged In: $isAnonymous")
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