package com.lukaarmen.gamezone.ui.tabs.home.live_matches_list.match_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lukaarmen.domain.common.success
import com.lukaarmen.domain.model.MatchDomain
import com.lukaarmen.domain.use_case.matches.FetchMatchPlayersUseCase
import com.lukaarmen.domain.use_case.matches.GetMatchByIdUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.util.ViewState
import com.lukaarmen.gamezone.model.Match
import com.lukaarmen.gamezone.model.MatchPlayers
import com.lukaarmen.gamezone.model.toMatch
import com.lukaarmen.gamezone.model.toMatchPlayers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MatchDetailsViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getMatchByIdUseCase: GetMatchByIdUseCase,
    private val fetchMatchPlayersUseCase: FetchMatchPlayersUseCase
) : BaseViewModel() {

    private val teamsId = mutableListOf<Int?>()

    private val _matchState = MutableStateFlow(ViewState<Match>())
    val matchState = _matchState.asStateFlow()

    private val _matchPlayers = MutableStateFlow<List<MatchPlayers>>(emptyList())
    val matchPlayers = _matchPlayers.asStateFlow()

    init {
        viewModelScope.launch {
            getMatchById()
        }
    }

    suspend fun getMatchById() {
        stateHandler(getMatchByIdUseCase(savedStateHandle.get<Int>("matchId")!!).map {
            it.onSuccess { matchDomain -> prepareData(matchDomain) }
            it.success { matchDomain -> matchDomain.toMatch() }
        }, _matchState.value).collect {
            _matchState.emit(it)
        }
    }

    private suspend fun prepareData(matchDomain: MatchDomain) {
        matchDomain.opponents?.let { teamsList ->
            teamsList.forEach { teamDomain ->
                teamDomain?.id?.let { id -> teamsId.add(id) } ?: teamsId.add(0)
            }
            _matchPlayers.emit(fetchMatchPlayersUseCase(teamsId).map { matchPlayersDomain ->
                matchPlayersDomain.toMatchPlayers()
            })
        }
    }
}