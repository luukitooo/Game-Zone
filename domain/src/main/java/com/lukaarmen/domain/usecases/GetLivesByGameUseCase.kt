package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.MatchesRepository
import javax.inject.Inject

class GetLivesByGameUseCase @Inject constructor(private val matchesRepository: MatchesRepository) {

    suspend operator fun invoke(gameType: String) =
        matchesRepository.getRunningMatchesByGame(gameType, 1, 50, "begin_at")

}