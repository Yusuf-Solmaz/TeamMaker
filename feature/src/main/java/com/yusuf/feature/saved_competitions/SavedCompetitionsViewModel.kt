package com.yusuf.feature.saved_competitions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.use_cases.firebase_use_cases.competition.DeleteSavedCompetitionUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.competition.GetAllSavedCompetitionsUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.saved_competitions.state.DeleteSavedCompetitionsUIState
import com.yusuf.feature.saved_competitions.state.GetSavedCompetitionsUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedCompetitionsViewModel @Inject constructor(
    private val getAllSavedCompetitionsUseCase: GetAllSavedCompetitionsUseCase,
    private val deleteSavedCompetitionUseCase: DeleteSavedCompetitionUseCase
) : ViewModel() {

    private val _getSavedCompetitionsUIState = MutableStateFlow(GetSavedCompetitionsUIState())
    val getSavedCompetitionsUIState : StateFlow<GetSavedCompetitionsUIState> = _getSavedCompetitionsUIState

    private val _deleteSavedCompetitionUIState = MutableStateFlow(DeleteSavedCompetitionsUIState())
    val deleteSavedCompetitionUIState : StateFlow<DeleteSavedCompetitionsUIState> = _deleteSavedCompetitionUIState

    init {
        getSavedCompetitions()
    }

    private fun getSavedCompetitions() {
        _getSavedCompetitionsUIState.value =
            _getSavedCompetitionsUIState.value.copy(isLoading = true)
        viewModelScope.launch {
            getAllSavedCompetitionsUseCase().collect { result ->
                when (result) {
                    is RootResult.Error -> {
                        _getSavedCompetitionsUIState.value =
                            _getSavedCompetitionsUIState.value.copy(
                                error = result.message,
                                isLoading = false,
                                savedCompetitions = emptyList()
                            )
                    }

                    is RootResult.Loading -> {
                        _getSavedCompetitionsUIState.value =
                            _getSavedCompetitionsUIState.value.copy(
                                isLoading = true,
                                error = null,
                                savedCompetitions = emptyList()
                            )
                    }

                    is RootResult.Success -> {
                        _getSavedCompetitionsUIState.value =
                            _getSavedCompetitionsUIState.value.copy(
                                savedCompetitions = result.data,
                                isLoading = false,
                                error = null
                            )
                    }
                }
            }
        }
    }

    fun deleteSavedCompetition(competitionId: String) {
        _deleteSavedCompetitionUIState.value =
            _deleteSavedCompetitionUIState.value.copy(isLoading = true)
        viewModelScope.launch {
            deleteSavedCompetitionUseCase(competitionId).collect { result ->
                when (result) {
                    is RootResult.Loading -> {
                        _deleteSavedCompetitionUIState.value =
                            _deleteSavedCompetitionUIState.value.copy(
                                isLoading = true,
                                error = null,
                                transaction = false
                            )
                    }
                    is RootResult.Error -> {
                        _deleteSavedCompetitionUIState.value =
                            _deleteSavedCompetitionUIState.value.copy(
                                error = result.message,
                                isLoading = false,
                                transaction = false
                            )
                    }
                    is RootResult.Success -> {
                        _deleteSavedCompetitionUIState.value =
                            _deleteSavedCompetitionUIState.value.copy(
                                isLoading = false,
                                error = null,
                                transaction = true
                            )
                        getSavedCompetitions()
                    }
                }
            }
        }
    }
}