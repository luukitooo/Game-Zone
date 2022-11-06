package com.lukaarmen.domain.use_case.matches

import com.lukaarmen.domain.repository.MatchesRepository
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val repository: MatchesRepository
) {

    suspend operator fun invoke(
        leagueId: Int,
        gameType: String,
        timeFrame: String,
        sort: String,
        title: String,
    ) = repository.getMatchesByLeagueId(
        leagueId = leagueId,
        gameType = gameType,
        timeFrame = timeFrame,
        sort = sort,
        title = title
    )

}