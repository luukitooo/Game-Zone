package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.MatchesRepository
import javax.inject.Inject

class GetMatchesUseCase @Inject constructor(
    private val repository: MatchesRepository
) {

    suspend operator fun invoke(
        leagueId: Int,
        page: Int,
        perPage: Int,
        gameType: String,
        timeFrame: String,
        sort: String,
        title: String,
    ) = repository.getMatchesByLeagueId(
        leagueId = leagueId,
        page = page,
        perPage = perPage,
        gameType = gameType,
        timeFrame = timeFrame,
        sort = sort,
        title = title
    )

}