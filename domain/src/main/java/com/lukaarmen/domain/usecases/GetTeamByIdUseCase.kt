package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.TeamsRepository
import javax.inject.Inject

class GetTeamByIdUseCase @Inject constructor(
    private val teamsRepository: TeamsRepository
) {

    suspend operator fun invoke(teamId: Int) = teamsRepository.getTeamById(teamId)

}