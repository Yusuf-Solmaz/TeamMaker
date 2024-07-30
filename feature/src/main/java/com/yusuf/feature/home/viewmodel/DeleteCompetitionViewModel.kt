package com.yusuf.feature.home.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.use_cases.firebase_use_cases.user.DeleteCompetitionUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.home.state.HomeScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeleteCompetitionViewModel @Inject constructor(
    private val deleteCompetitionUseCase: DeleteCompetitionUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState: StateFlow<HomeScreenState> = _uiState

    fun deleteCompetition(competitionData: CompetitionData) {
        _uiState.value = _uiState.value.copy(isLoading = true, deleteCompetitionResult = RootResult.Loading)
        viewModelScope.launch {
            deleteCompetitionUseCase(competitionData).collect { result ->
                _uiState.value = _uiState.value.copy(
                    isLoading = result is RootResult.Loading,
                    deleteCompetitionResult = result
                )
                if (result is RootResult.Success) {
                    Log.d("DeleteCompetitionViewModel", "Competition deleted successfully")
                } else if (result is RootResult.Error) {
                    Log.e("DeleteCompetitionViewModel", "Error deleting competition: ${result.message}")
                }
            }
        }
    }
}