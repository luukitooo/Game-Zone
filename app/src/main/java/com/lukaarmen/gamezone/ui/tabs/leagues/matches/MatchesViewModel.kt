package com.lukaarmen.gamezone.ui.tabs.leagues.matches

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.use_case.matches.GetMatchesUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.util.TimeFrame
import com.lukaarmen.gamezone.common.util.ViewState
import com.lukaarmen.gamezone.model.Match
import com.lukaarmen.gamezone.model.toMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchesViewModel @Inject constructor(
    private val getMatchesUseCase: GetMatchesUseCase,
    private val savedStateHandler: SavedStateHandle
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            getMatchesByLeagueId(
                leagueId = savedStateHandler.get<Int>("leagueId") ?: -1,
                gameType = savedStateHandler.get<String>("gameType") ?: "",
                timeFrame = TimeFrame.PAST.timeFrame
            )
        }
    }

    private val _matchesFlow = MutableStateFlow(ViewState<List<Match>>())
    val matchesFlow get() = _matchesFlow.asStateFlow()

    suspend fun getMatchesByLeagueId(
        leagueId: Int,
        gameType: String,
        timeFrame: String,
        sort: String = "begin_at",
        title: String = ""
    ) {
        stateHandler(
            state = getMatchesUseCase(
                leagueId = leagueId,
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