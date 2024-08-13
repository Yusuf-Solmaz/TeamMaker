package com.yusuf.feature.create_competition.select_player.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.use_cases.firebase_use_cases.image.UploadImageUseCase
import com.yusuf.domain.use_cases.team.TeamBalancerUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.create_competition.select_player.state.TeamBalancerUIState
import com.yusuf.feature.create_competition.select_player.state.TeamImageUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamBalancerViewModel @Inject constructor(
    private val teamBalancerUseCase: TeamBalancerUseCase,
    private val uploadImageUseCase: UploadImageUseCase
) : ViewModel() {

    private val _teamBalancerUiState = MutableStateFlow(TeamBalancerUIState())
    val teamBalancerUiState: StateFlow<TeamBalancerUIState> get() = _teamBalancerUiState

    private val _teamImageUIState = MutableStateFlow(TeamImageUIState())
    val teamImageUIState: StateFlow<TeamImageUIState> get() = _teamImageUIState

    internal fun uploadImage(uri: Uri) {
        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            uploadImageUseCase(uri, "saved_competitions").collect { result ->
                when (result) {
                    is RootResult.Success -> {
                        _teamImageUIState.value = _teamImageUIState.value.copy(
                            imageUri = result.data,
                            isLoading = false,
                            error = null
                        )
                    }

                    is RootResult.Error -> {
                        _teamImageUIState.value = _teamImageUIState.value.copy(
                            imageUri = null,
                            isLoading = false,
                            error = result.message
                        )
                    }

                    RootResult.Loading -> {
                        _teamImageUIState.value = _teamImageUIState.value.copy(
                            isLoading = true,
                            error = null,
                            imageUri = null
                        )

                    }
                }
            }
        }
    }

    internal fun createBalancedTeams(players: List<PlayerData>) {
        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(2000)
            teamBalancerUseCase(players).collect { result ->
                when (result) {
                    is RootResult.Success -> {
                        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                            teams = result.data,
                            isLoading = false,
                            errorMessage = null
                        )
                        Log.d(
                            "SharedViewModel",
                            "Teams set: ${_teamBalancerUiState.value.teams}"
                        )
                    }

                    is RootResult.Error -> {
                        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                            teams = null,
                            isLoading = false,
                            errorMessage = result.message
                        )
                        Log.e(
                            "SharedViewModel",
                            "Error creating balanced teams: ${result.message}"
                        )
                    }

                    is RootResult.Loading -> {
                        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                            isLoading = true
                        )
                        Log.d("SharedViewModel", "Loading creating balanced teams...")
                    }
                }
            }
        }
    }
}
