package com.lukaarmen.gamezone.ui.tabs.leagues.leaguesfragment

import androidx.lifecycle.viewModelScope
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.usecases.GetLeaguesUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.utils.CategoryIndicator
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.models.League
import com.lukaarmen.gamezone.models.toLeague
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val getLeaguesUseCase: GetLeaguesUseCase
) : BaseViewModel() {

    init {
        viewModelScope.launch {
            getLeagues(GameType.CSGO.title)
        }
    }

    private val gameIndicators = mutableListOf(
        CategoryIndicator(GameType.CSGO, true),
        CategoryIndicator(GameType.DOTA2, false),
        CategoryIndicator(GameType.OWERWATCH, false),
        CategoryIndicator(GameType.RAINBOW_SIX, false),
    )

    private val _leaguesFlow = MutableStateFlow(ViewState<List<League>>())
    val leaguesFlow get() = _leaguesFlow.asStateFlow()

    private val _indicatorsFlow = MutableStateFlow(gameIndicators)
    val indicatorsFlow get() = _indicatorsFlow.asStateFlow()

    suspend fun getLeagues(
        gameType: String,
        page: Int = 1,
        perPage: Int = 50,
        name: String? = null
    ) {
        stateHandler(getLeaguesUseCase(gameType, page, perPage, name).map { resource ->
            resource.mapSuccess { domain ->
                domain.toLeague()
            }
        }, _leaguesFlow.value).collect { state ->
            _leaguesFlow.emit(state)
        }
    }

    suspend fun updateIndicators(selectedIndicator: GameType) {
        val updatedGameIndicators = mutableListOf<CategoryIndicator>()
        gameIndicators.forEach { gameIndicator ->
            if (gameIndicator.gameType.title == selectedIndicator.title) {
                updatedGameIndicators.add(
                    CategoryIndicator(
                        gameType = gameIndicator.gameType,
                        isSelected = true
                    )
                )
            } else {
                updatedGameIndicators.add(
                    CategoryIndicator(
                        gameType = gameIndicator.gameType,
                        isSelected = false
                    )
                )
            }
        }
        _indicatorsFlow.emit(updatedGameIndicators)
    }

}