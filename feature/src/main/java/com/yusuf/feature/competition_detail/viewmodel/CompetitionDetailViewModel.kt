package com.yusuf.feature.competition_detail.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.domain.use_cases.firebase_use_cases.competition.SaveCompetitionsUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.competition_detail.state.CompetitionDetailUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitionDetailViewModel @Inject constructor(
    private val saveCompetitionsUseCase: SaveCompetitionsUseCase
) : ViewModel() {

    private val _competitionDetailState = MutableStateFlow(CompetitionDetailUIState())
    val competitionDetailState: StateFlow<CompetitionDetailUIState> = _competitionDetailState

    internal fun saveCompetition(savedCompetitionsModel: SavedCompetitionsModel){
        _competitionDetailState.value = _competitionDetailState.value.copy(isLoading = true)

        viewModelScope.launch {
            saveCompetitionsUseCase(savedCompetitionsModel).collect { result ->
                _competitionDetailState.value = CompetitionDetailUIState(
                    isLoading = result is RootResult.Loading,
                    transaction = result is RootResult.Success,
                    error = if (result is RootResult.Error) result.message else null
                )
            }
        }
    }
}