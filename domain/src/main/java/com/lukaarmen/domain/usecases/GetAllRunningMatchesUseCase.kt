package com.lukaarmen.domain.usecases

import com.lukaarmen.domain.repositories.MatchesRepository
import javax.inject.Inject

class GetAllRunningMatchesUseCase @Inject constructor(private val matchesRepository: MatchesRepository) {

    suspend operator fun invoke() = matchesRepository.getAllRunningMatches(1, 6, "begin_at")

}