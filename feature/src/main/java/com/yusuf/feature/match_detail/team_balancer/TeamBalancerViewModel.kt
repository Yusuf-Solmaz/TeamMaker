package com.yusuf.feature.match_detail.team_balancer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.use_cases.team.TeamBalancerUseCase
import com.yusuf.domain.util.RootResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamBalancerViewModel @Inject constructor(
    private val teamBalancerUseCase: TeamBalancerUseCase
) : ViewModel(){
    private val _uiState = MutableStateFlow(TeamBalancerUIState())
    val uiState : StateFlow<TeamBalancerUIState> get() = _uiState

    fun createBalancedTeams(players: List<PlayerData>) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                teamBalancerUseCase(players).collect { result ->
                    _uiState.value = _uiState.value.copy(
                        teams = result,
                        isLoading = false,
                        errorMessage = null
                    )
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