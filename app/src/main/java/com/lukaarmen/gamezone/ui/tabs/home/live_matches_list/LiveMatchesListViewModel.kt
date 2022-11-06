package com.lukaarmen.gamezone.ui.tabs.home.live_matches_list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.use_case.matches.GetAllRunningMatchesUseCase
import com.lukaarmen.domain.use_case.matches.GetRunningMatchesByGameUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.util.GameType
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
class LiveMatchesListViewModel @Inject constructor(
    private val getRunningMatchesByGameUseCase: GetRunningMatchesByGameUseCase,
    private val getAllRunningMatchesUseCase: GetAllRunningMatchesUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _livesState = MutableStateFlow(ViewState<List<Match>>())
    val livesState = _livesState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchMatches()
        }
    }

    suspend fun fetchMatches(matchName: String? = null) {
        if (savedStateHandle.get<String>("gameType")!! == GameType.ALL.title) {
            getAllLives(matchName)
        } else {
            getLivesByGame(savedStateHandle.get<String>("gameType")!!, matchName)
        }
    }

    private suspend fun getLivesByGame(game: String, name: String?) {
        stateHandler(getRunningMatchesByGameUseCase(game, name).map {
            it.mapSuccess { domain ->
                domain.toMatch()
            }
        }, _livesState.value).collect { _livesState.value = it }
    }

    private suspend fun getAllLives(name: String?) {
        stateHandler(getAllRunningMatchesUseCase(name).map {
            it.mapSuccess { matchDomain ->
                matchDomain.toMatch()
            }
        }, _livesState.value).collect { _livesState.value = it }
    }

}