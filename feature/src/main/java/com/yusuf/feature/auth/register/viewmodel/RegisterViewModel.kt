package com.yusuf.feature.auth.register.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.use_cases.firebase_use_cases.auth.RegisterUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.AddCompetitionUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.auth.register.state.RegisterUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val addCompetitionUseCase: AddCompetitionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUIState())
    val uiState: StateFlow<RegisterUIState> = _uiState

    internal fun signUp(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            registerUseCase(email, password)
                .collect { result ->
                    when (result) {
                        is RootResult.Success -> {
                            runAddDefaultCompetitions(addCompetitionUseCase)

                            result.data?.let { user ->
                                _uiState.value = _uiState.value.copy(
                                    user = user,
                                    isLoading = false
                                )
                            }
                        }
                        is RootResult.Error -> {
                            _uiState.value =
                                _uiState.value.copy(error = result.message, isLoading = false)
                        }
                        is RootResult.Loading -> {
                            _uiState.value = _uiState.value.copy(isLoading = true)
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
}