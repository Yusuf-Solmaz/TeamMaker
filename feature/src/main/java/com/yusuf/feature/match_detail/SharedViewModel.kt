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

    private val _uiState = MutableStateFlow(TeamBalancerUIState())
    val uiState: StateFlow<TeamBalancerUIState> get() = _uiState

    fun setSelectedPlayers(players: List<PlayerData>) {
        _selectedPlayers.value = players
    }

    fun createBalancedTeams() {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val players = _selectedPlayers.value
                teamBalancerUseCase(players).collect { result ->
                    _uiState.value = _uiState.value.copy(
                        teams = result,
                        isLoading = false,
                        errorMessage = null
                    )
                    Log.d("SharedViewModel", "Balanced teams set: $result")
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    errorMessage = e.message,
                    isLoading = false
                )
            }
        }
    }
}