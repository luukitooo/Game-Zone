package com.lukaarmen.gamezone.ui.tabs.leagues

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.use_case.leagues.GetLeaguesUseCase
import com.lukaarmen.domain.use_case.leagues.AddFavoriteLeagueUseCase
import com.lukaarmen.domain.use_case.leagues.GetAllFavoriteLeaguesUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.model.CategoryIndicator
import com.lukaarmen.gamezone.common.util.GameType
import com.lukaarmen.gamezone.common.util.ViewState
import com.lukaarmen.gamezone.model.FavoriteLeague
import com.lukaarmen.gamezone.model.League
import com.lukaarmen.gamezone.model.toLeague
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaguesViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val getLeaguesUseCase: GetLeaguesUseCase,
    private val gatAllFavoriteLeaguesUseCase: GetAllFavoriteLeaguesUseCase,
    private val addFavoriteLeaguesUseCase: AddFavoriteLeagueUseCase,
    private val getAllFavoriteLeaguesUseCase: GetAllFavoriteLeaguesUseCase
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

    private var searchQuery = ""

    private val _leaguesFlow = MutableStateFlow(ViewState<List<League>>())
    val leaguesFlow get() = _leaguesFlow.asStateFlow()

    private val _indicatorsFlow = MutableStateFlow(gameIndicators)
    val indicatorsFlow get() = _indicatorsFlow.asStateFlow()

    private val _isLeagueAlreadySavedFlow = MutableSharedFlow<FavoriteLeague?>()
    val isLeagueAlreadySavedFlow get() = _isLeagueAlreadySavedFlow.asSharedFlow()

    suspend fun getLeagues(
        gameType: String = _indicatorsFlow.value.find { it.isSelected }?.gameType?.title ?: "",
        name: String? = searchQuery,
        withLoader: Boolean = true
    ) {
        _leaguesFlow.emit(ViewState())
        val savedLeagues = getAllFavoriteLeaguesUseCase.invoke().filter { favoriteLeague ->
            favoriteLeague.userId == auth.currentUser!!.uid
        }
        stateHandler(getLeaguesUseCase(gameType, name).map { resource ->
            resource.mapSuccess { domain ->
                val league = domain.toLeague()
                savedLeagues.find { favorite ->
                    league.id == favorite.leagueId
                }?.let { league.isSaved = true }
                league
            }
        }, _leaguesFlow.value, withLoader).collect { state ->
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

    suspend fun checkLeagueSaved(league: FavoriteLeague) {
        if (gatAllFavoriteLeaguesUseCase().contains(league.toFavoriteLeagueDomain())) {
            _isLeagueAlreadySavedFlow.emit(null)
        } else {
            _isLeagueAlreadySavedFlow.emit(league)
        }
    }

    fun setSearchQuery(leagueTitle: String) {
        searchQuery = leagueTitle
    }

    suspend fun addLeagueToFavorites(league: FavoriteLeague) {
        addFavoriteLeaguesUseCase(league.toFavoriteLeagueDomain())
    }

    fun clearState() {
        _leaguesFlow.value = ViewState()
    }

}