package com.yusuf.domain.repository.firebase.competition

import com.yusuf.domain.model.firebase.SavedCompetitionsModel
import com.yusuf.domain.util.RootResult
import kotlinx.coroutines.flow.Flow

interface CompetitionRepository {
    suspend fun saveCompetitions(savedCompetitionsModel: SavedCompetitionsModel) : Flow<RootResult<Boolean>>
    suspend fun getAllSavedCompetitions(): Flow<RootResult<List<SavedCompetitionsModel>>>
    suspend fun deleteSavedCompetition(competitionId: String): Flow<RootResult<Boolean>>
}