package com.yusuf.domain.use_cases.team

import com.yusuf.domain.model.firebase.PlayerData
import com.yusuf.domain.repository.team.TeamBalancerRepository
import javax.inject.Inject

class TeamBalancerUseCase @Inject constructor(
    private val teamBalancerRepository: TeamBalancerRepository
) {
    operator fun invoke(players: List<PlayerData>) = teamBalancerRepository.createBalancedTeams(players)
}