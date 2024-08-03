package com.yusuf.feature.match_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.use_cases.team.TeamBalancerUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.match_detail.team_balancer.TeamBalancerUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val teamBalancerUseCase: TeamBalancerUseCase
) : ViewModel() {
    private val _teamBalancerUiState = MutableStateFlow(TeamBalancerUIState())
    val teamBalancerUiState: StateFlow<TeamBalancerUIState> get() = _teamBalancerUiState

    fun createBalancedTeams(players: List<PlayerData>) {
        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            delay(2000)
            teamBalancerUseCase(players).collect { result ->
                when (result) {
                    is RootResult.Success -> {
                        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                            teams = result,
                            isLoading = false,
                            errorMessage = null
                        )
                        Log.d("SharedViewModel", "Teams set: ${_teamBalancerUiState.value.teams}")
                    }
                    is RootResult.Error -> {
                        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                            teams = null,
                            isLoading = false,
                            errorMessage = result.message
                        )
                        Log.e("SharedViewModel", "Error creating balanced teams: ${result.message}")
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

