package com.lukaarmen.domain.use_case.matches

import com.lukaarmen.domain.repository.MatchesRepository
import javax.inject.Inject

class GetRunningMatchesByGameUseCase @Inject constructor(private val matchesRepository: MatchesRepository) {

    suspend operator fun invoke(gameType: String, name: String?) =
        matchesRepository.getRunningMatchesByGame(gameType, "-begin_at", name)

}