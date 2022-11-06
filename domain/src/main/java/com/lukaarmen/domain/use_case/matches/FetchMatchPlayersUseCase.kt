package com.lukaarmen.domain.use_case.matches

import com.lukaarmen.domain.model.MatchPlayersDomain
import com.lukaarmen.domain.repository.TeamsRepository
import javax.inject.Inject

class FetchMatchPlayersUseCase @Inject constructor(
    private val teamsRepository: TeamsRepository
) {

    suspend operator fun invoke(teamsIdList: List<Int?>): MutableList<MatchPlayersDomain> {
        val matchPlayersList = mutableListOf<MatchPlayersDomain>()

        if (teamsIdList.first() == 0) matchPlayersList.map { matchPlayers ->
            matchPlayers.firstTeamPlayer = null
        } else {
            teamsIdList.first()?.let { firstTeamId ->
                teamsRepository.getTeamById(firstTeamId).collect {
                    it.onSuccess { teamsDomain ->
                        teamsDomain.players?.forEach { playerDomain ->
                            matchPlayersList.add(MatchPlayersDomain(firstTeamPlayer = playerDomain))
                        }
                    }
                }
            }
        }

        if (teamsIdList.last() == 0) matchPlayersList.map { matchPlayers ->
            matchPlayers.secondTeamPlayer = null
        }
        else {
            teamsIdList.last()?.let { secondTeamId ->
                teamsRepository.getTeamById(secondTeamId).collect {
                    it.onSuccess { teamDomain ->
                        for ((index, value) in teamDomain.players?.withIndex()!!) {
                            if (matchPlayersList.size > index) {
                                matchPlayersList[index].secondTeamPlayer = value
                            } else {
                                matchPlayersList.add(MatchPlayersDomain(null, value))
                            }
                        }
                    }
                }
            }
        }
        return matchPlayersList
    }
}
