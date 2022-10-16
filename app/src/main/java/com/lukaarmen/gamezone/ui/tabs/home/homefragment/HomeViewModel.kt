package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lukaarmen.domain.common.Resource
import com.lukaarmen.domain.models.MatchDomain
import com.lukaarmen.domain.usecases.GetAllRunningMatchesUseCase
import com.lukaarmen.domain.usecases.GetLivesByGameUseCase
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.models.Match
import com.lukaarmen.gamezone.models.toMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getAllRunningMatchesUseCase: GetAllRunningMatchesUseCase,
    private val getLivesByGameUseCase: GetLivesByGameUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ViewState<Match>())
    val viewState = _viewState.asStateFlow()

    suspend fun getAllRunningMatches() {
        viewModelScope.launch {
            getAllRunningMatchesUseCase().collect {
                statesHandler(it)
            }
        }
    }

    suspend fun getLivesByGame(gameType: String){
        viewModelScope.launch {
            getLivesByGameUseCase(gameType).collect {
                statesHandler(it)
            }
        }
    }

    private suspend fun statesHandler(value: Resource<List<MatchDomain>>){
        value.apply {
            val currentState = _viewState.value
            onSuccess { matchesList ->
                _viewState.emit(currentState.copy(data = matchesList.map { matchDomain ->
                    matchDomain.toMatch()
                }))
            }
            onFailure { errorMsg ->
                _viewState.emit(currentState.copy(error = errorMsg))
            }
            onLoader { isLoading ->
                _viewState.emit(currentState.copy(isLoading = isLoading))
            }
        }
    }

}