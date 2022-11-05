package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.MatchesRepository
import javax.inject.Inject

class GetAllRunningMatchesUseCase @Inject constructor(private val matchesRepository: MatchesRepository) {

    suspend operator fun invoke(name: String?) = matchesRepository.getAllRunningMatches(
        sort = "-begin_at",
        filter = "cs-go,dota-2,ow,r6-siege",
        name = name
    )

}