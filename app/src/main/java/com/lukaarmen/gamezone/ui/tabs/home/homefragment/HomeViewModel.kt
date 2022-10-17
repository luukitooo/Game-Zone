package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.usecases.GetAllRunningMatchesUseCase
import com.lukaarmen.domain.usecases.GetLivesByGameUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.models.Match
import com.lukaarmen.gamezone.models.toMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
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

    suspend fun getAllRunningMatches() {
        stateHandler(
            getAllRunningMatchesUseCase().map {
                it.onSuccess {list ->
                    _streamsCountState.emit(list.size)
                }
                it.onFailure { list ->
                    _streamsCountState.emit(0)
                }
                it.mapSuccess { domain ->
                    domain.toMatch()
                }
            },
            _viewState.value
        ).collect {
            _viewState.emit(it)
        }
    }

    suspend fun getLivesByGame(gameType: String) {
        stateHandler(
            getLivesByGameUseCase(gameType).map {
                it.mapSuccess { domain -> domain.toMatch() }
            },
            _viewState.value
        ).collect {
            _viewState.emit(it)
        }
    }
}

