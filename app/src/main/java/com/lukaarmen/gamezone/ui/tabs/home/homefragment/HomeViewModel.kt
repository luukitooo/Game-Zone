package com.lukaarmen.gamezone.ui.tabs.home.homefragment

import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.lukaarmen.domain.common.mapSuccess
import com.lukaarmen.domain.usecases.GetAllRunningMatchesUseCase
import com.lukaarmen.domain.usecases.GetLivesByGameUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.common.utils.CategoryIndicator
import com.lukaarmen.gamezone.common.utils.GameType
import com.lukaarmen.gamezone.common.utils.ViewState
import com.lukaarmen.gamezone.model.User
import com.lukaarmen.gamezone.models.Match
import com.lukaarmen.gamezone.models.toMatch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @Named("Users") private val usersReference: DatabaseReference,
    private val getAllRunningMatchesUseCase: GetAllRunningMatchesUseCase,
    private val getLivesByGameUseCase: GetLivesByGameUseCase
) : BaseViewModel() {

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
        updateProfile()
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

    fun updateProfile() {
        usersReference.child(firebaseAuth.currentUser!!.uid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = try {
                    snapshot.getValue(User::class.java) ?: return
                } catch(t : Throwable) {
                    return
                }
                _userState.value = user
            }

            override fun onCancelled(error: DatabaseError) {
                throw Exception(error.message)
            }
        })
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
            getLivesByGameUseCase(gameType, null).map {
                it.onSuccess { matchesList -> _streamsCountState.emit(matchesList.size) }
                it.mapSuccess { matchDomain -> matchDomain.toMatch() }
            }, _viewState.value
        ).collect {
            _viewState.value = it
        }
    }
}

