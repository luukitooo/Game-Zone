package com.lukaarmen.gamezone.ui.tabs.favorites.favoritesfragment

import com.lukaarmen.domain.usecases.favorite_leagues.AddFavoriteLeagueUseCase
import com.lukaarmen.domain.usecases.favorite_leagues.GetFavoriteLeagueByTitleUseCase
import com.lukaarmen.domain.usecases.favorite_leagues.GetFavoriteLeaguesFlowByUidUseCase
import com.lukaarmen.domain.usecases.favorite_leagues.RemoveFavoriteLeagueUseCase
import com.lukaarmen.gamezone.common.base.BaseViewModel
import com.lukaarmen.gamezone.models.FavoriteLeague
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteLeaguesFlowByUidUseCase: GetFavoriteLeaguesFlowByUidUseCase,
    private val removeFavoriteLeagueUseCase: RemoveFavoriteLeagueUseCase,
    private val addFavoritesLeagueUseCase: AddFavoriteLeagueUseCase,
    private val getFavoriteLeaguesByTitleUseCase: GetFavoriteLeagueByTitleUseCase
) : BaseViewModel() {

    fun favoriteLeaguesFlow(uid: String) = getFavoriteLeaguesFlowByUidUseCase(uid)

    suspend fun removeLeague(league: FavoriteLeague) {
        removeFavoriteLeagueUseCase(
            league.toFavoriteLeagueDomain()
        )
    }

    suspend fun addLeague(league: FavoriteLeague) {
        addFavoritesLeagueUseCase(
            league.toFavoriteLeagueDomain()
        )
    }

}