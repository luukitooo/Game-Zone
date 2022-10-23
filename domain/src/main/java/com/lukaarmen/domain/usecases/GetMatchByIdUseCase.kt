package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.MatchesRepository
import javax.inject.Inject

class GetMatchByIdUseCase @Inject constructor(
    private val matchesRepository: MatchesRepository
) {

    suspend operator fun invoke(matchId: Int) = matchesRepository.getMatchById(matchId)

}