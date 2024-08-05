package com.yusuf.feature.create_competition.select_player.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.use_cases.team.CalculateTeamAverageSkillRatingUseCase
import com.yusuf.domain.use_cases.team.TeamBalancerUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.create_competition.select_player.state.TeamBalancerUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TeamBalancerViewModel @Inject constructor(
    private val teamBalancerUseCase: TeamBalancerUseCase,
    private val calculateTeamAverageSkillRatingUseCase: CalculateTeamAverageSkillRatingUseCase
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
                        result.data?.let { (teamFirst, teamSecond) ->
                            val teamFirstAverage = calculateTeamAverageSkillRatingUseCase(teamFirst)
                            val teamSecondAverage = calculateTeamAverageSkillRatingUseCase(teamSecond)

                            _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                                teams = result.data,
                                teamAverageSkillRating = teamFirstAverage to teamSecondAverage,
                                isLoading = false,
                                errorMessage = null
                            )
                            Log.d("TeamBalancerViewModel", "Teams set: ${_teamBalancerUiState.value.teams}")
                            Log.d("TeamBalancerViewModel", "Team Average Skill Ratings: $teamFirstAverage and $teamSecondAverage")
                        } ?: run {
                            _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                                teams = null,
                                teamAverageSkillRating = null,
                                isLoading = false,
                                errorMessage = "Teams data is null"
                            )
                            Log.e("TeamBalancerViewModel", "Teams data is null")
                        }
                    }
                    is RootResult.Error -> {
                        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                            teams = null,
                            teamAverageSkillRating = null,
                            isLoading = false,
                            errorMessage = result.message
                        )
                        Log.e("TeamBalancerViewModel", "Error creating balanced teams: ${result.message}")
                    }
                    is RootResult.Loading -> {
                        _teamBalancerUiState.value = _teamBalancerUiState.value.copy(
                            isLoading = true
                        )
                        Log.d("TeamBalancerViewModel", "Loading creating balanced teams...")
                    }
                }
            }
        }
    }
}

