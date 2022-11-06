package com.lukaarmen.domain.use_case.matches

import com.lukaarmen.domain.repository.TeamsRepository
import javax.inject.Inject

class GetTeamByIdUseCase @Inject constructor(
    private val teamsRepository: TeamsRepository
) {

    suspend operator fun invoke(teamId: Int) = teamsRepository.getTeamById(teamId)

}