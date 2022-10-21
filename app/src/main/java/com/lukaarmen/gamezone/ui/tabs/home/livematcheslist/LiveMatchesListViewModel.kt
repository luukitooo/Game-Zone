package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.usecases.GetAllRunningMatchesUseCase
import com.lukaarmen.domain.usecases.GetLivesByGameUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
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
class LiveMatchesListViewModel @Inject constructor(
    private val getLivesByGameUseCase: GetLivesByGameUseCase,
    private val getAllRunningMatchesUseCase: GetAllRunningMatchesUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _livesState = MutableStateFlow(ViewState<List<Match>>())
    val livesState = _livesState.asStateFlow()

    init {
        viewModelScope.launch {
            fetchMatches(null)
        }
    }

    suspend fun fetchMatches(name: String?){
        if(savedStateHandle.get<String>("gameType")!! == GameType.ALL.title){
            getAllLives(name)
        }else{
            getLivesByGame(savedStateHandle.get<String>("gameType")!!, name)
        }
    }

    private suspend fun getLivesByGame(game: String, name: String?){
        stateHandler(getLivesByGameUseCase(game, name).map {
            it.mapSuccess { domain ->
                domain.toMatch()
            }
        }, _livesState.value).collect{_livesState.value = it}
    }

    private suspend fun getAllLives(name: String?){
        stateHandler(getAllRunningMatchesUseCase(name).map {
            it.mapSuccess { matchDomain ->
                matchDomain.toMatch()
            }
        }, _livesState.value).collect{_livesState.value = it}
    }

}