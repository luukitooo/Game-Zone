package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.MatchesRepository
import javax.inject.Inject

class GetAllRunningMatchesUseCase @Inject constructor(private val matchesRepository: MatchesRepository) {

    suspend operator fun invoke() = matchesRepository.getAllRunningMatches(
        null,
        null,
        "begin_at",
        "cs-go,dota-2,ow,r6-siege"
    )

}