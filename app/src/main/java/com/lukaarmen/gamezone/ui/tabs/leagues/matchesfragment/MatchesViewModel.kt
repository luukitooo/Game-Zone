package com.lukaarmen.gamezone.ui.tabs.leagues.matchesfragment

import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.usecases.GetMatchesUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.models.Match
import com.lukaarmen.gamezone.models.toMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase
) : BaseViewModel() {

    private val _matchesFlow = MutableStateFlow(ViewState<List<Match>>())
    val matchesFlow get() = _matchesFlow.asStateFlow()

    suspend fun getMatchesByLeagueId(
        leagueId: Int,
        page: Int = 1,
        perPage: Int = 0,
        gameType: String,
        timeFrame: String,
        sort: String = "begin_at",
        title: String = ""
    ) {
        stateHandler(
            state = getMatchesUseCase(
                leagueId = leagueId,
                page = page,
                perPage = perPage,
                gameType = gameType,
                timeFrame = timeFrame,
                sort = sort,
                title = title
            ).map { resource ->
                resource.mapSuccess { domain -> domain.toMatch() }
            },
            currentState = _matchesFlow.value
        ). collect { state ->
            _matchesFlow.emit(state)
        }
    }

}