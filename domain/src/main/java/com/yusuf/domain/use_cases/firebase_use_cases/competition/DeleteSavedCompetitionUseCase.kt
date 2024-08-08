package com.yusuf.domain.use_cases.firebase_use_cases.competition

import com.yusuf.domain.repository.firebase.competition.CompetitionRepository
import javax.inject.Inject

class DeleteSavedCompetitionUseCase @Inject constructor(
    private val competitionRepository: CompetitionRepository
) {
    suspend operator fun invoke(competitionId: String) = competitionRepository.deleteSavedCompetition(competitionId)
}