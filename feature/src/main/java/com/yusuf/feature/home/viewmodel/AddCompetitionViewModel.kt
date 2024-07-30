package com.yusuf.feature.home.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.use_cases.firebase_use_cases.user.AddCompetitionUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.home.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCompetitionViewModel @Inject constructor(
    private val addCompetitionUseCase: AddCompetitionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState

    fun addCompetition(competitionData: CompetitionData) {
        _uiState.value = _uiState.value.copy(isLoading = true, addCompetitionResult = RootResult.Loading)
        viewModelScope.launch {
            addCompetitionUseCase(competitionData).collect { result ->
                _uiState.value = _uiState.value.copy(
                    isLoading = result is RootResult.Loading,
                    addCompetitionResult = result
                )
            }
        }
    }
}