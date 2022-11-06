package com.lukaarmen.gamezone.ui.tabs.home

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.use_case.matches.GetAllRunningMatchesUseCase
import com.lukaarmen.domain.use_case.matches.GetRunningMatchesByGameUseCase
import com.lukaarmen.domain.use_case.users.GetUserByIdUseCase
import com.lukaarmen.domain.use_case.users.ObserveUserByIdUseCase
import com.lukaarmen.domain.use_case.users.UpdateUserActivityUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.util.ActivityStatus
import com.lukaarmen.gamezone.model.CategoryIndicator
import com.lukaarmen.gamezone.common.util.GameType
import com.lukaarmen.gamezone.common.util.ViewState
import com.lukaarmen.gamezone.model.User
import com.lukaarmen.gamezone.model.Match
import com.lukaarmen.gamezone.model.toMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val getUserByIdUseCase: GetUserByIdUseCase,
    private val getAllRunningMatchesUseCase: GetAllRunningMatchesUseCase,
    private val getRunningMatchesByGameUseCase: GetRunningMatchesByGameUseCase,
    private val observeUserByIdUseCase: ObserveUserByIdUseCase,
    private val updateUserActivityUseCase: UpdateUserActivityUseCase
) : BaseViewModel() {

    suspend fun setStatusToOnline() {
        updateUserActivityUseCase.invoke(
            uid = firebaseAuth.currentUser!!.uid,
            ActivityStatus.IS_ACTIVE
        )
    }

    private val gamesList = mutableListOf(
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
        viewModelScope.launch {
            updateProfile()
        }
    }

    private val _viewState = MutableStateFlow(ViewState<List<Match>>())
    val viewState = _viewState.asStateFlow()

    private val _streamsCountState = MutableStateFlow(0)
    val streamsCountState = _streamsCountState.asStateFlow()

    private val _gamesListState = MutableStateFlow<List<CategoryIndicator>>(gamesList)
    val gamesListState = _gamesListState.asStateFlow()

    private val _userState = MutableStateFlow(User())
    val userState = _userState.asStateFlow()

    suspend fun updateGamesList(selectedGame: GameType) {
        var position = 0
        val updatedGameList = mutableListOf<CategoryIndicator>()
        updatedGameList.addAll(gamesList)
        gamesList.forEach {
            if (it.gameType == selectedGame) position = updatedGameList.indexOf(it)
            if (it.isSelected) {
                updatedGameList[updatedGameList.indexOf(it)] =
                    CategoryIndicator(gameType = it.gameType, isSelected = false)
            }
        }
        updatedGameList[position] =
            CategoryIndicator(gameType = gamesList[position].gameType, isSelected = true)

        _gamesListState.emit(updatedGameList)

        when (selectedGame.title) {
            GameType.ALL.title -> getAllRunningMatches()
            else -> getLivesByGame(selectedGame.title)
        }
    }

    private suspend fun updateProfile() {
        observeUserByIdUseCase.invoke(firebaseAuth.currentUser!!.uid) { userDomain ->
            viewModelScope.launch {
                _userState.emit(
                    User.fromDomain(userDomain)
                )
            }
        }
    }

    private suspend fun getAllRunningMatches() {
        stateHandler(
            getAllRunningMatchesUseCase(null).map {
                it.onSuccess { matchesList -> _streamsCountState.emit(matchesList.size) }
                it.mapSuccess { matchDomain -> matchDomain.toMatch() }
            }, _viewState.value
        ).collect {
            _viewState.value = it
        }
    }

    private suspend fun getLivesByGame(gameType: String) {
        stateHandler(
            getRunningMatchesByGameUseCase(gameType, null).map {
                it.onSuccess { matchesList -> _streamsCountState.emit(matchesList.size) }
                it.mapSuccess { matchDomain -> matchDomain.toMatch() }
            }, _viewState.value
        ).collect {
            _viewState.value = it
        }
    }

}

