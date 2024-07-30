package com.yusuf.feature.home.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.use_cases.firebase_use_cases.user.AddCompetitionUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.DeleteCompetitionUseCase
import com.yusuf.domain.use_cases.firebase_use_cases.user.GetAllCompetitionsUseCase
import com.yusuf.domain.util.RootResult
import com.yusuf.feature.home.state.AddDeleteState
import com.yusuf.feature.home.state.GetAllState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompetitionViewModel @Inject constructor(
    private val addCompetitionUseCase: AddCompetitionUseCase,
    private val deleteCompetitionUseCase: DeleteCompetitionUseCase,
    private val getAllCompetitionsUseCase: GetAllCompetitionsUseCase
) : ViewModel() {

    private val _addDeleteState = MutableStateFlow(AddDeleteState())
    val addDeleteState: StateFlow<AddDeleteState> = _addDeleteState

    private val _getAllState = MutableStateFlow(GetAllState())
    val getAllState: StateFlow<GetAllState> = _getAllState

    fun addCompetition(competitionData: CompetitionData) {
        _addDeleteState.value = AddDeleteState(isLoading = true, result = RootResult.Loading)
        viewModelScope.launch {
            addCompetitionUseCase(competitionData).collect { result ->
                _addDeleteState.value = AddDeleteState(
                    isLoading = result is RootResult.Loading,
                    result = result,
                    error = if (result is RootResult.Error) result.message else null
                )
                if (result is RootResult.Success) {
                    getAllCompetitions()
                }
            }
        }
    }

    fun deleteCompetition(competitionData: CompetitionData) {
        _addDeleteState.value = AddDeleteState(isLoading = true, result = RootResult.Loading)
        viewModelScope.launch {
            deleteCompetitionUseCase(competitionData).collect { result ->
                _addDeleteState.value = AddDeleteState(
                    isLoading = result is RootResult.Loading,
                    result = result,
                    error = if (result is RootResult.Error) result.message else null
                )
                if (result is RootResult.Success) {
                    getAllCompetitions()
                }
            }
        }
    }

    fun getAllCompetitions() {
        _getAllState.value = GetAllState(isLoading = true, result = RootResult.Loading)
        viewModelScope.launch {
            getAllCompetitionsUseCase().collect { result ->
                _getAllState.value = GetAllState(
                    isLoading = result is RootResult.Loading,
                    competitions = if (result is RootResult.Success) result.data ?: emptyList() else emptyList(),
                    result = result,
                    error = if (result is RootResult.Error) result.message else null
                )
            }
        }
    }
}
