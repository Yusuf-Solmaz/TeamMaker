package com.yusuf.domain.use_cases.firebase_use_cases.user

import com.yusuf.domain.model.firebase.CompetitionData
import com.yusuf.domain.repository.firebase.player.PlayerRepository
import javax.inject.Inject

class AddCompetitionUseCase @Inject constructor(
    private val playerRepository: PlayerRepository
) {
    suspend operator fun invoke(competitionData: CompetitionData) = playerRepository.addCompetition(competitionData)
}