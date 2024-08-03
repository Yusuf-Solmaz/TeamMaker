package com.yusuf.feature.match_detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.use_cases.team.TeamBalancerUseCase
import com.yusuf.feature.match_detail.team_balancer.TeamBalancerUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val teamBalancerUseCase: TeamBalancerUseCase
) : ViewModel() {
    private val _selectedPlayers = MutableStateFlow<List<PlayerData>>(emptyList())
    val selectedPlayers: StateFlow<List<PlayerData>> get() = _selectedPlayers

    private val _teamBalancerUiState = MutableStateFlow(TeamBalancerUIState())
    val teamBalancerUiState: StateFlow<TeamBalancerUIState> get() = _teamBalancerUiState

    fun setSelectedPlayers(players: List<PlayerData>) {
        _selectedPlayers.value = players
    }

    fun createBalancedTeams() {
        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val players = _selectedPlayers.value
                teamBalancerUseCase(players).collect { result ->
                    _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                        teams = result,
                        isLoading = false,
                        errorMessage = null
                    )
                    Log.d("SharedViewModel", "Players set: ${players.size}")
                    Log.d("SharedViewModel", "Create Balanced teams set: $result")
                }
            } catch (e: Exception) {
                _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun isTeamsReady(): Boolean {
        val teamsReady = _teamBalancerUiState.value.teams != null
        Log.d("SharedViewModel", "Teams ready: $teamsReady")
        return teamsReady
    }
}
