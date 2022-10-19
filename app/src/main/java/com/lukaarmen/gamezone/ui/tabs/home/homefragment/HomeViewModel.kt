package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import androidx.lifecycle.viewModelScope
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.usecases.GetAllRunningMatchesUseCase
import com.lukaarmen.domain.usecases.GetLivesByGameUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.utils.CategoryIndicator
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.models.Match
import com.lukaarmen.gamezone.models.toMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRunningMatchesUseCase: GetAllRunningMatchesUseCase,
    private val getLivesByGameUseCase: GetLivesByGameUseCase
) : BaseViewModel() {

    private val _viewState = MutableStateFlow(ViewState<List<Match>>())
    val viewState = _viewState.asStateFlow()

    private val _streamsCountState = MutableStateFlow(0)
    val streamsCountState = _streamsCountState.asStateFlow()

    val gamesList = mutableListOf(
        CategoryIndicator(GameType.ALL, true),
        CategoryIndicator(GameType.CSGO, false),
        CategoryIndicator(GameType.DOTA2, false),
        CategoryIndicator(GameType.OWERWATCH, false),
        CategoryIndicator(GameType.RAINBOW_SIX, false),
    )

    init {
        viewModelScope.launch {
            getAllRunningMatches()
        }
    }

    suspend fun getAllRunningMatches() {
        stateHandler(
            getAllRunningMatchesUseCase().map {
                it.onSuccess { list ->
                    _streamsCountState.emit(list.size)
                }
                it.mapSuccess { domain ->
                    domain.toMatch()
                }
            },
            _viewState.value
        ).collect {
            _viewState.value = it
        }
    }

    suspend fun getLivesByGame(gameType: String) {
        stateHandler(
            getLivesByGameUseCase(gameType).map {
                it.mapSuccess { domain -> domain.toMatch() }
            },
            _viewState.value
        ).collect {
            _viewState.value = it
        }
    }
}

