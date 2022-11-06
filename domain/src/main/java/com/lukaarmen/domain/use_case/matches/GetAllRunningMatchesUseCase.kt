package com.lukaarmen.domain.use_case.matches

import com.lukaarmen.domain.repository.MatchesRepository
import javax.inject.Inject

class GetAllRunningMatchesUseCase @Inject constructor(private val matchesRepository: MatchesRepository) {

    suspend operator fun invoke(name: String?) = matchesRepository.getAllRunningMatches(
        sort = "-begin_at",
        filter = "cs-go,dota-2,ow,r6-siege",
        name = name
    )

}