package com.lukaarmen.gamezone.ui.tabs.home.livematcheslist

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LiveMatchesListViewModel @Inject constructor(
    private val getLivesByGameUseCase: GetLivesByGameUseCase,
    private val getAllRunningMatchesUseCase: GetAllRunningMatchesUseCase,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val _viewState = MutableStateFlow(ViewState<List<Match>>())
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            if(savedStateHandle.get<String>("gameType")!! == "all"){
                getAllLives()
            }else{
                getLivesByGame(savedStateHandle.get<String>("gameType")!!)
            }
        }
    }

    suspend fun getLivesByGame(game: String){
        stateHandler(getLivesByGameUseCase(game).map {
            it.mapSuccess { domain ->
                domain.toMatch()
            }
        }, _viewState.value).collect{_viewState.value = it}
    }

    suspend fun getAllLives(){
        stateHandler(getAllRunningMatchesUseCase().map {
            it.mapSuccess { matchDomain ->
                matchDomain.toMatch()
            }
        }, _viewState.value).collect{_viewState.value = it}
    }

}